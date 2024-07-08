package ru.cft.template.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.api.Paths;
import ru.cft.template.api.dto.SessionDto;
import ru.cft.template.api.dto.SessionRequest;
import ru.cft.template.core.service.serviceImpl.SessionService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Session", description = "Операции над сессиями: авторизация, выход из сессии, получении информации о сессии")
public class SessionController {

    private final SessionService sessionService;


    @GetMapping(Paths.USERS_SESSIONS)
    @Operation(description = "Получить все активные сессии")
    public List<SessionDto> getAll() {
        return sessionService.getAllActive();
    }


    @GetMapping(Paths.USERS_SESSIONS_CURRENT)
    @Operation(description = "Получить текущию сессию")
    public SessionDto getCurrent() {
        return sessionService.getCurrent();
    }


    @PostMapping(Paths.USERS_SESSIONS)
    @Operation(description = "Создает новую сессию для пользователя.")
    public SessionDto login(@Valid @RequestBody SessionRequest sessionRequest) {
        return sessionService.create(sessionRequest);
    }


    @DeleteMapping(Paths.USERS_SESSIONS_ID)
    @Operation(description = "Завершает текущую сессию пользователя.")
    public void remove() {
        sessionService.removeCurrent();
    }
}
