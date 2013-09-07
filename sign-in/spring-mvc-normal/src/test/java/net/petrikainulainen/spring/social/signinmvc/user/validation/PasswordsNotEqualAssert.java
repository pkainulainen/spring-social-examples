package net.petrikainulainen.spring.social.signinmvc.user.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author Petri Kainulainen
 */
public class PasswordsNotEqualAssert extends ConstraintViolationAssert<PasswordsNotEqualDTO> {

    private static final String VALIDATION_ERROR_MESSAGE = "PasswordsNotEqual";

    public PasswordsNotEqualAssert(Set<ConstraintViolation<PasswordsNotEqualDTO>> actual) {
        super(PasswordsNotEqualAssert.class, actual);
    }

    public static PasswordsNotEqualAssert assertThat(Set<ConstraintViolation<PasswordsNotEqualDTO>> actual) {
        return new PasswordsNotEqualAssert(actual);
    }

    @Override
    protected String getErrorMessage() {
        return VALIDATION_ERROR_MESSAGE;
    }
}
