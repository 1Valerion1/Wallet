package ru.cft.template.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.UserCreateRequest;
import ru.cft.template.api.dto.UserDto;
import ru.cft.template.api.dto.UserPatchRequest;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.ConflictException;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.mapper.UserMapper;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import static ru.cft.template.core.Messages.USER_NOT_FOUND;
import static ru.cft.template.core.exception.ErrorCode.OBJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserDto getById(String id) {
        return userMapper.map(getByIdOrThrow(id));
    }

    public void create(UserCreateRequest userCreateRequest) {
        User user = buildNewUser(userCreateRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.existsByEmail(user.getEmail()) &&
                userRepository.existsByPhone(user.getPhone())) {
            throw new ConflictException("User with this email or phone already exists");
        }
        Wallet wallet = new Wallet();
        wallet.setBalance(100);
        wallet.setUser(user);


        try {
            userRepository.save(user);
            walletRepository.save(wallet);
        } catch (ConstraintViolationException ex) {
            throw new ConflictException("User with this email or phone already exists" + ex);
        } catch (RuntimeException ex) {
            throw ex;
        }

    }


    private User buildNewUser(UserCreateRequest dto) {
        return User.builder()
                .lastName(dto.lastName())
                .firstName(dto.firstName())
                .patronymic(dto.patronymic())
                .birthday(dto.birthday())
                .phone(dto.phone())
                .email(dto.email())
                .enabled(true)
                .password(dto.password())
                .build();
    }

    public UserDto update(UserPatchRequest userPatchRequest) {
        User user = Context.get().getUser();
        userMapper.map(user, userPatchRequest);
        userRepository.save(user);
        return userMapper.map(user);
    }

    private User getByIdOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(OBJECT_NOT_FOUND, String.format(USER_NOT_FOUND, id)));
    }
}
