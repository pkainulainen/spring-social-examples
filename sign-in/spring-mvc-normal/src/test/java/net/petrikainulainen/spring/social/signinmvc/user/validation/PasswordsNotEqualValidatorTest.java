package net.petrikainulainen.spring.social.signinmvc.user.validation;

import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static net.petrikainulainen.spring.social.signinmvc.user.validation.PasswordsNotEqualAssert.assertThat;

/**
 * @author Petri Kainulainen
 */
public class PasswordsNotEqualValidatorTest {

    private static final String PASSWORD = "password";
    private static final String PASSWORD_VERIFICATION = "passwordVerification";

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void passwordsNotEqual_BothPasswordsAreNull_ShouldPassValidation() {
        PasswordsNotEqualDTO passesValidation = PasswordsNotEqualDTO.getBuilder().build();

        assertThat(validator.validate(passesValidation)).hasNoValidationErrors();
    }

    @Test
    public void passwordsNotEqual_PasswordIsNull_ShouldReturnValidationErrorsForBothFields() {
        PasswordsNotEqualDTO failsValidation = PasswordsNotEqualDTO.getBuilder()
                .passwordVerification(PASSWORD_VERIFICATION)
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(2)
                .hasValidationErrorForField("password")
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEqual_PasswordVerificationIsNull_ShouldReturnValidationErrorsForBothFields() {
        PasswordsNotEqualDTO failsValidation = PasswordsNotEqualDTO.getBuilder()
                .password(PASSWORD)
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(2)
                .hasValidationErrorForField("password")
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEqual_BothPasswordsAreEmpty_ShouldPassValidation() {
        PasswordsNotEqualDTO passesValidation = PasswordsNotEqualDTO.getBuilder()
                .password("")
                .passwordVerification("")
                .build();

        assertThat(validator.validate(passesValidation)).hasNoValidationErrors();
    }

    @Test
    public void passwordsNotEqual_PasswordIsEmpty_ShouldReturnValidationErrorsForBothFields() {
        PasswordsNotEqualDTO failsValidation = PasswordsNotEqualDTO.getBuilder()
                .password("")
                .passwordVerification(PASSWORD_VERIFICATION)
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(2)
                .hasValidationErrorForField("password")
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEqual_PasswordVerificationIsEmpty_ShouldReturnValidationErrorsForBothFields() {
        PasswordsNotEqualDTO failsValidation = PasswordsNotEqualDTO.getBuilder()
                .password(PASSWORD)
                .passwordVerification("")
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(2)
                .hasValidationErrorForField("password")
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEqual_PasswordMismatch_ShouldReturnValidationErrorsForBothFields() {
        PasswordsNotEqualDTO failsValidation = PasswordsNotEqualDTO.getBuilder()
                .password(PASSWORD)
                .passwordVerification(PASSWORD_VERIFICATION)
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(2)
                .hasValidationErrorForField("password")
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEqual_PasswordsMatch_ShouldPassValidation() {
        PasswordsNotEqualDTO passesValidation = PasswordsNotEqualDTO.getBuilder()
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        assertThat(validator.validate(passesValidation)).hasNoValidationErrors();
    }

    @Test(expected = ValidationException.class)
    public void passwordsNotEqual_InvalidPasswordField_ShouldThrowException() {
        InvalidPasswordFieldDTO invalid = new InvalidPasswordFieldDTO();

        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void passwordsNotEqual_InvalidPasswordVerificationField_ShouldThrowException() {
        InvalidPasswordVerificationFieldDTO invalid = new InvalidPasswordVerificationFieldDTO();

        validator.validate(invalid);
    }

    @PasswordsNotEqual(
            passwordFieldName = "password",
            passwordVerificationFieldName = "passwordVerification"
    )
    private class InvalidPasswordFieldDTO {
        private String passwordVerification;
    }

    @PasswordsNotEqual(
            passwordFieldName = "password",
            passwordVerificationFieldName = "passwordVerification"
    )
    private class InvalidPasswordVerificationFieldDTO {
        private String password;
    }
}
