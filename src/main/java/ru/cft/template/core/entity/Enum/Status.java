package ru.cft.template.core.entity.Enum;

import com.fasterxml.jackson.annotation.JsonFormat;

public enum Status {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    PAID,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    UNPAID,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    CANCELLED;
}
