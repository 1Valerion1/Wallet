package ru.cft.template.core.service.serviceImpl;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.cft.template.api.dto.SessionDto;
import ru.cft.template.api.dto.SessionRequest;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Sessions;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.mapper.SessionMapper;
import ru.cft.template.core.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final AuthenticationManager authenticationManager;

    @Nullable
    public Sessions getValidSession(String token) {
        Sessions sessions = sessionRepository.findByValue(token);
        if (sessions != null && sessions.getExpirationTime().isBefore(LocalDateTime.now())) {
            invalidate(sessions);
            return null;
        }
        return sessions;
    }

    public List<SessionDto> getAllActive() {
        User user = Context.get().getUser();
        List<Sessions> sessions = sessionRepository
                .findAllByUserAndActiveTrueAndExpirationTimeAfter(user, LocalDateTime.now());

        return sessionMapper.map(sessions);
    }

    public SessionDto getCurrent() {
        return sessionMapper.map(Context.get().getSessions());
    }

    public SessionDto create(SessionRequest sessionRequest) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(sessionRequest.email(), sessionRequest.password()));

        User user = (User) authentication.getPrincipal();
        List<Sessions> activeSessions = sessionRepository.findAllByUserAndActiveTrue(user);
        if (activeSessions.size() >= 3) {
            throw new RuntimeException("Достигнут предел количества активных сессий для данного пользователя");
        }

        Sessions sessions = buildNewSession((User) authentication.getPrincipal());
        sessionRepository.save(sessions);
        return sessionMapper.map(sessions);
    }

    public void removeCurrent() {
        Sessions sessions = Context.get().getSessions();
        invalidate(sessions);
    }

    public void invalidateExpiredSessions() {
        sessionRepository.updateAllByExpirationTimeAfter(LocalDateTime.now());
    }

    public void cleanupExpiredSessions() {
        sessionRepository.deleteAllByExpirationTimeAfter();
    }

    private void invalidate(Sessions sessions) {
        sessions.setActive(false);
    }

    private Sessions buildNewSession(User user) {
        return Sessions.builder()
                .active(true)
                .expirationTime(LocalDateTime.now().plusHours(1))
                .user(user)
                .build();
    }
}
