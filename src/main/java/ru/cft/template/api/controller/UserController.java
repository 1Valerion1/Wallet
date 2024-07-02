package ru.cft.template.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.dto.UserCreateRequest;
import ru.cft.template.api.dto.UserDto;
import ru.cft.template.api.dto.UserPatchRequest;
import ru.cft.template.core.service.UserService;

import static ru.cft.template.api.Paths.USERS;
import static ru.cft.template.api.Paths.USER_ID;

@Tag(name = "Пользователи")
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(description = """
            Получение информации о пользователе по его идентификатору.
            """)
    @GetMapping(USER_ID)
    public UserDto get(@PathVariable String id) {
        return userService.getById(id);
    }

    @Operation(description = """
            Регистрация нового пользователя. Сессия при регистрации не создаётся.
            """)
    @PostMapping(USERS)
    public void create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        userService.create(userCreateRequest);
    }

    @Operation(description = """
            Обновление информации о пользователе пользователе.
            """)
    @PatchMapping(USERS)
    public UserDto update(@Valid @RequestBody UserPatchRequest userPatchRequest) {
        return userService.update(userPatchRequest);
    }
}
