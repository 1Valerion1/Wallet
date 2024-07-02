package ru.cft.template.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Headers {
    public static String SESSION_HEADER_NAME = "X-Session-Token";
}
