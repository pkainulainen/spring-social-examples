package net.petrikainulainen.spring.social.signinmvc.user.validation;

import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static net.petrikainulainen.spring.social.signinmvc.user.validation.PasswordsNotEmptyAssert.assertThat;

/**
 * @author Petri Kainulainen
 */
public class PasswordsNotEmptyValidatorTest {

    private static final String PASSWORD = "password";
    private static final String PASSWORD_VERIFICATION = "passwordVerification";
    private static final String TRIGGER = "trigger";

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void passwordsNotEmpty_TriggerFieldIsNotNull_ShouldValidateNothing() {
        PasswordsNotEmptyDTO notValidated = PasswordsNotEmptyDTO.getBuilder()
                .trigger(TRIGGER)
                .build();

        assertThat(validator.validate(notValidated)).hasNoValidationErrors();
    }

    @Test
    public void passwordsNotEmpty_TriggerFieldNullAndPasswordFieldsHaveValues_ShouldPassValidation() {
        PasswordsNotEmptyDTO passesValidation = PasswordsNotEmptyDTO.getBuilder()
                .password(PASSWORD)
                .passwordVerification(PASSWORD_VERIFICATION)
                .trigger(TRIGGER)
                .build();

        assertThat(validator.validate(passesValidation)).hasNoValidationErrors();
    }

    @Test
    public void passwordsNotEmpty_TriggerFieldAndPasswordFieldsAreNull_ShouldReturnValidationErrorForPasswordField() {
        PasswordsNotEmptyDTO failsValidation = PasswordsNotEmptyDTO.getBuilder()
                .passwordVerification(PASSWORD_VERIFICATION)
                .build();

            assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(1)
                .hasValidationErrorForField("password");
    }

    @Test
    public void passwordsNotEmpty_TriggerFieldIsNullAndPasswordFieldIsEmpty_ShouldReturnValidationErrorForPasswordField() {
        PasswordsNotEmptyDTO failsValidation = PasswordsNotEmptyDTO.getBuilder()
                .password("")
                .passwordVerification(PASSWORD_VERIFICATION)
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(1)
                .hasValidationErrorForField("password");
    }


    @Test
    public void passwordsNotEmpty_TriggerFieldAndPasswordVerificationFieldsAreNull_ShouldReturnValidationErrorForPasswordVerificationField() {
        PasswordsNotEmptyDTO failsValidation = PasswordsNotEmptyDTO.getBuilder()
                .password(PASSWORD)
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(1)
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEmpty_TriggerFieldIsNullAndPasswordVerificationFieldIsEmpty_ShouldReturnValidationErrorForPasswordVerificationField() {
        PasswordsNotEmptyDTO failsValidation = PasswordsNotEmptyDTO.getBuilder()
                .password(PASSWORD)
                .passwordVerification("")
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(1)
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEmpty_TriggerFieldIsNullAndBothPasswordFieldsAreNull_ShouldReturnValidationErrorsForBothFields() {
        PasswordsNotEmptyDTO failsValidation = PasswordsNotEmptyDTO.getBuilder().build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(2)
                .hasValidationErrorForField("password")
                .hasValidationErrorForField("passwordVerification");
    }

    @Test
    public void passwordsNotEmpty_TriggerFieldIsNullAndBothPasswordFieldsAreEmpty_ShouldReturnValidationErrorsForBothFields() {
        PasswordsNotEmptyDTO failsValidation = PasswordsNotEmptyDTO.getBuilder()
                .password("")
                .passwordVerification("")
                .build();

        assertThat(validator.validate(failsValidation))
                .numberOfValidationErrorsIs(2)
                .hasValidationErrorForField("password")
                .hasValidationErrorForField("passwordVerification");
    }

    @Test(expected = ValidationException.class)
    public void passwordsNotEmpty_InvalidTriggerField_ShouldThrowException() {
        InvalidTriggerFieldDTO invalid = new InvalidTriggerFieldDTO();

        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void passwordsNotEmpty_InvalidPasswordField_ShouldThrowException() {
        InvalidPasswordFieldDTO invalid = new InvalidPasswordFieldDTO();

        validator.validate(invalid);
    }

    @Test(expected = ValidationException.class)
    public void passwordsNotEmpty_InvalidPasswordVerificationField_ShouldThrowException() {
        InvalidPasswordVerificationFieldDTO invalid = new InvalidPasswordVerificationFieldDTO();

        validator.validate(invalid);
    }

    @PasswordsNotEmpty(
            triggerFieldName = "trigger",
            passwordFieldName = "password",
            passwordVerificationFieldName = "passwordVerification"
    )
    private class InvalidTriggerFieldDTO {

        private String password;
        private String passwordVerification;
    }

    @PasswordsNotEmpty(
            triggerFieldName = "trigger",
            passwordFieldName = "password",
            passwordVerificationFieldName = "passwordVerification"
    )
    private class InvalidPasswordFieldDTO {

        private String trigger;
        private String passwordVerification;
    }

    @PasswordsNotEmpty(
            triggerFieldName = "trigger",
            passwordFieldName = "password",
            passwordVerificationFieldName = "passwordVerification"
    )
    private class InvalidPasswordVerificationFieldDTO {

        private String trigger;
        private String password;
    }

}
