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
import ru.cft.template.core.service.SessionService;

import java.util.List;

@Tag(name = "Сессии")
@Validated
@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @Operation(description = """
            Получение всех активных сессий пользователя.
            """)
    @GetMapping(Paths.USERS_SESSIONS)
    public List<SessionDto> getAll() {
        return sessionService.getAllActive();
    }

    @Operation(description = """
            Получение информации о текущей сессии пользователя.
            """)
    @GetMapping(Paths.USERS_SESSIONS_CURRENT)
    public SessionDto getCurrent() {
        return sessionService.getCurrent();
    }

    @Operation(description = """
            Создание новой сессии для пользователя.
            """)
    @PostMapping(Paths.USERS_SESSIONS)
    public SessionDto login(@Valid @RequestBody SessionRequest sessionRequest) {
        return sessionService.create(sessionRequest);
    }

    @Operation(description = """
            Удаление текущей сессии пользователя.
            """)
    @DeleteMapping(Paths.USERS_SESSIONS_ID)
    public void remove() {
        sessionService.removeCurrent();
    }
}
