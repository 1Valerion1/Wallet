package ru.cft.template.api.dto;

import lombok.Builder;

@Builder
public record HesoyamDto (

     Boolean success,
     Integer newBalance) {
}
