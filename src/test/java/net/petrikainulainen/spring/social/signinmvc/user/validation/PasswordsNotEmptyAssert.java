package net.petrikainulainen.spring.social.signinmvc.user.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author Petri Kainulainen
 */
public class PasswordsNotEmptyAssert extends ConstraintViolationAssert<PasswordsNotEmptyDTO> {

    private static final String VALIDATION_ERROR_MESSAGE = "PasswordsNotEmpty";

    public PasswordsNotEmptyAssert(Set<ConstraintViolation<PasswordsNotEmptyDTO>> actual) {
        super(PasswordsNotEmptyAssert.class, actual);
    }

    public static PasswordsNotEmptyAssert assertThat(Set<ConstraintViolation<PasswordsNotEmptyDTO>> actual) {
        return new PasswordsNotEmptyAssert(actual);
    }

    @Override
    protected String getErrorMessage() {
        return VALIDATION_ERROR_MESSAGE;
    }
}
