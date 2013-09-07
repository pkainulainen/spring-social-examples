package net.petrikainulainen.spring.social.signinmvc.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author Petri Kainulainen
 */
public class PasswordsNotEmptyValidator implements ConstraintValidator<PasswordsNotEmpty, Object> {

    private String validationTriggerFieldName;
    private String passwordFieldName;
    private String passwordVerificationFieldName;

    @Override
    public void initialize(PasswordsNotEmpty constraintAnnotation) {
        validationTriggerFieldName = constraintAnnotation.triggerFieldName();
        passwordFieldName = constraintAnnotation.passwordFieldName();
        passwordVerificationFieldName = constraintAnnotation.passwordVerificationFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        try {
            Object validationTrigger = getFieldValue(value, validationTriggerFieldName);
            if (validationTrigger == null) {
                return passWordFieldsAreValid(value, context);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Exception occurred during validation", ex);
        }

        return true;
    }

    private boolean passWordFieldsAreValid(Object value, ConstraintValidatorContext context) throws NoSuchFieldException, IllegalAccessException {
        boolean passwordWordFieldsAreValid = true;

        String password = (String) getFieldValue(value, passwordFieldName);
        if (isNullOrEmpty(password)) {
            addValidationError(passwordFieldName, context);
            passwordWordFieldsAreValid = false;
        }

        String passwordVerification = (String) getFieldValue(value, passwordVerificationFieldName);
        if (isNullOrEmpty(passwordVerification)) {
            addValidationError(passwordVerificationFieldName, context);
            passwordWordFieldsAreValid = false;
        }

        return passwordWordFieldsAreValid;
    }

    private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = object.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(object);
    }

    private boolean isNullOrEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    private void addValidationError(String field, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addNode(field)
                .addConstraintViolation();
    }
}
