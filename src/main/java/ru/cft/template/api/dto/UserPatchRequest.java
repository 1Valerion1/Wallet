package ru.cft.template.api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserPatchRequest(

        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String firstName,

        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String lastName) {
}
