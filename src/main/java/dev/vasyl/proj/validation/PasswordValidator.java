package dev.vasyl.proj.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    public static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    public static final String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    public static final String PASSWORD_SPECIAL_CHAR_REGEX = ".*[!@#$%^&*(),.?\":{}|<>].*";
    public static final String PASSWORD_MIN_LENGTH_REGEX = ".{8,}";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        if (!password.matches(PASSWORD_MIN_LENGTH_REGEX)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must be at least 8 characters long")
                    .addConstraintViolation();
            return false;
        }

        if (!password.matches(PASSWORD_UPPERCASE_REGEX)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one uppercase letter")
                    .addConstraintViolation();
            return false;
        }

        if (!password.matches(PASSWORD_DIGIT_REGEX)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one digit")
                    .addConstraintViolation();
            return false;
        }

        if (!password.matches(PASSWORD_SPECIAL_CHAR_REGEX)) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one special character")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
