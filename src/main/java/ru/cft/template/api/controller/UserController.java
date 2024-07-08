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
import ru.cft.template.core.service.serviceImpl.UserService;

import static ru.cft.template.api.Paths.USERS;
import static ru.cft.template.api.Paths.USER_ID;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "Операции над пользователем: регистрация,обновление и получения данных о пользователе")
public class UserController {
    private final UserService userService;
    @GetMapping(USER_ID)
    @Operation(description = "Возвращает информацию о конкретном пользователе по его идентификатору")
    public UserDto get(@PathVariable String id) {
        return userService.getById(id);
    }


    @PostMapping(USERS)
    @Operation(description = "Создает нового пользователя")
    public void create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        userService.create(userCreateRequest);
    }


    @PatchMapping(USERS)
    @Operation(description = "Обновляет поля ФИО и дату рождения пользователя по его идентификатору")
    public UserDto update(@Valid @RequestBody UserPatchRequest userPatchRequest) {
        return userService.update(userPatchRequest);
    }
}
