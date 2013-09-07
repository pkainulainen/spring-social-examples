package net.petrikainulainen.spring.social.signinmvc.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author Petri Kainulainen
 */
public class PasswordsNotEqualValidator implements ConstraintValidator<PasswordsNotEqual, Object> {

    private String passwordFieldName;

    private String passwordVerificationFieldName;

    @Override
    public void initialize(PasswordsNotEqual constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.passwordFieldName();
        this.passwordVerificationFieldName = constraintAnnotation.passwordVerificationFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        try {
            String password = (String) getFieldValue(value, passwordFieldName);
            String passwordVerification = (String) getFieldValue(value, passwordVerificationFieldName);

            if (passwordsAreNotEqual(password, passwordVerification)) {
                addValidationError(passwordFieldName, context);
                addValidationError(passwordVerificationFieldName, context);

                return false;
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Exception occurred during validation", ex);
        }

        return true;
    }

    private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = object.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(object);
    }

    private boolean passwordsAreNotEqual(String password, String passwordVerification) {
        return !(password == null ? passwordVerification == null : password.equals(passwordVerification));
    }

    private void addValidationError(String field, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addNode(field)
                .addConstraintViolation();
    }
}
