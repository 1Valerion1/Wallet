package ru.cft.template.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.cft.template.core.Context;
import ru.cft.template.core.entity.Sessions;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.service.serviceImpl.SessionService;

import java.io.IOException;

import static ru.cft.template.api.Headers.SESSION_HEADER_NAME;

@Service
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {
    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String sessionToken = getTokenFromRequest(request);
        if (sessionToken != null) {
            Sessions sessions = sessionService.getValidSession(sessionToken);
            if (sessions != null) {
                User user = sessions.getUser();
                UsernamePasswordAuthenticationToken authenticationToken =
                        UsernamePasswordAuthenticationToken.authenticated(
                                user,
                                null,
                                user.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                Context.get().setSessions(sessions);
                Context.get().setUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(SESSION_HEADER_NAME);
        return StringUtils.hasText(token) ? token : null;
    }
}