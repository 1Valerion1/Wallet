package ru.cft.template.core.entity.Enum;

import com.fasterxml.jackson.annotation.JsonFormat;


public enum TransferType {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    INCOMING,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    OUTGOING;
}
