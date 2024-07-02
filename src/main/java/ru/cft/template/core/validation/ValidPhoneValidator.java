package ru.cft.template.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, Long> {

    private static final Pattern PHONE_NUMBER_PATTERN =  Pattern.compile("^7(\\d{10})$");

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return PHONE_NUMBER_PATTERN.matcher(String.valueOf(value)).matches();
    }
}

