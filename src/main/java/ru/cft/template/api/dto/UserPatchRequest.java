package ru.cft.template.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Schema(description = "Information about UserPatchRequest")
public record UserPatchRequest(

        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String firstName,
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String lastName,
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        String patronymic,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthday
)
{

}

