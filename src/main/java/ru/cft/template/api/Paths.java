package ru.cft.template.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Paths {
    public static final String USERS = "/users";
    public static final String USER_ID = "/users/{id}";
    public static final String USERS_SESSIONS = "/users/sessions";
    public static final String USERS_SESSIONS_CURRENT = "/users/sessions/current";
    public static final String USERS_SESSIONS_ID = "/users/sessions";
}
