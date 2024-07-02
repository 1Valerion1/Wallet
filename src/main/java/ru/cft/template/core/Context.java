package ru.cft.template.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cft.template.core.entity.Session;
import ru.cft.template.core.entity.User;

/**
 * Контекст http-запроса
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Context {

    private static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(Context::new);

    /**
     * Текущая сессия пользователя, если имеется
     */
    private Session session;
    /**
     * Текущий авторизованный пользователь, если имеется
     */
    private User user;

    public static Context get() {
        return CONTEXT.get();
    }
}
