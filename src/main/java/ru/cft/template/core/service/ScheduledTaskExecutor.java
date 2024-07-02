package ru.cft.template.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис выполнения заданий по расписанию
 */
@Service
@RequiredArgsConstructor
public class ScheduledTaskExecutor {
    private final SessionService service;

    /**
     * Инвалидация протухших на момент выполнения сессий
     */
    @Transactional
    @Scheduled(fixedDelayString = "PT60S")
    public void invalidateExpiredSessions() {
        service.invalidateExpiredSessions();
    }
}
