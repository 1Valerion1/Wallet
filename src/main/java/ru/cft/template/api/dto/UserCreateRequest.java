package ru.cft.template.api.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserCreateRequest (
        @NotNull
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        @Schema(description = "Фамилия пользователя", example = "Иванов")
        String lastName,
        @NotNull
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        @Schema(example = "Иванов")
        String firstName,
        @NotNull
        @Pattern(regexp = "^[А-Я][а-я]{0,49}$")
        @Schema(example = "Иванов")
        String patronymic,
        @NotNull
        @Pattern(regexp = "^7(\\d{10})$")
        @Schema(example = "79435352422")
        String phone,
        @Email
        @NotNull
        @Schema(example = "sdfsd@gmail.com")
        String email,

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthday,
        @NotNull
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[A-Za-z\\d!?]{8,64}$")
        @Schema(example = "Koroli1!")
        String password
) {
}
