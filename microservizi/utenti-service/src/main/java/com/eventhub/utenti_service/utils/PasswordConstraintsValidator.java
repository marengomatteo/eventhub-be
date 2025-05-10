package com.eventhub.utenti_service.utils;

import java.util.Arrays;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RepeatCharactersRule;
import org.passay.Rule;
import org.passay.RuleResult;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 64;
    private static final int MIN_UPPERCASE = 1;
    private static final int MIN_LOWERCASE = 1;
    private static final int MIN_DIGITS = 1;
    private static final int MIN_SPECIAL_CHARS = 1;
    private static final int MAX_CONSECUTIVE_REPEATS = 3;

    private Password password;

    @Override
    public void initialize(Password password) {
        this.password = password;
    }

    @Override
    public boolean isValid(String passwordInput, ConstraintValidatorContext constraintValidatorContext) {

        if (!password.required() && passwordInput == null) {
            return true;
        }

        List<Rule> rules = Arrays.asList(
                new LengthRule(MIN_LENGTH, MAX_LENGTH), // Lunghezza password
                new CharacterRule(EnglishCharacterData.UpperCase, MIN_UPPERCASE), // almeno un carattere maiuscolo
                new CharacterRule(EnglishCharacterData.LowerCase, MIN_LOWERCASE), // almeno un carattere minuscolo
                new CharacterRule(EnglishCharacterData.Digit, MIN_DIGITS), // almeno un numero
                new CharacterRule(EnglishCharacterData.Special, MIN_SPECIAL_CHARS), // almeno un carattere speciale
                new RepeatCharactersRule(MAX_CONSECUTIVE_REPEATS)// non più di due caratteri consecutivi
        );

        PasswordValidator passwordValidator = new PasswordValidator(rules);

        RuleResult result = passwordValidator.validate(new PasswordData(passwordInput));

        if (result.isValid()) {
            return true;
        }

        constraintValidatorContext
                .buildConstraintViolationWithTemplate(passwordValidator.getMessages(result).stream().findFirst().get())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;

    }
}