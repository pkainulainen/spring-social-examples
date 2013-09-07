package net.petrikainulainen.spring.social.signinmvc.user.validation;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author Petri Kainulainen
 */
public abstract class ConstraintViolationAssert<T> extends GenericAssert<ConstraintViolationAssert, Set<ConstraintViolation<T>>> {

    protected ConstraintViolationAssert(Class selfType, Set<ConstraintViolation<T>> actual) {
        super(selfType, actual);
    }

    protected abstract String getErrorMessage();

    public ConstraintViolationAssert hasNoValidationErrors() {
        isNotNull();

        String errorMessage = String.format(
                "Expected the size of constraint violations to be <0> but was <%d>",
                actual.size()
        );

        Assertions.assertThat(actual)
                .overridingErrorMessage(errorMessage)
                .isEmpty();

        return this;
    }

    public ConstraintViolationAssert hasValidationErrorForField(String fieldName) {
        isNotNull();

        boolean fieldFound = false;

        for (ConstraintViolation<T> violation: actual) {
            if (violation.getPropertyPath().toString().equals(fieldName)) {
                String errorMessage = String.format(
                        "Expected the error message to be <%s> but was <%s>",
                        getErrorMessage(),
                        violation.getMessage()
                );

                Assertions.assertThat(violation.getMessage())
                        .overridingErrorMessage(errorMessage)
                        .isEqualTo(getErrorMessage());

                fieldFound = true;
            }
        }

        String errorMessage = String.format(
                "Expected to find an error message for field <%s> but found none",
                fieldName
        );

        Assertions.assertThat(fieldFound)
                .overridingErrorMessage(errorMessage)
                .isTrue();

        return this;
    }

    public ConstraintViolationAssert numberOfValidationErrorsIs(int errorCount) {
        isNotNull();

        String errorMessage = String.format(
                "Expected to found <%d> validation errors but found <%d>",
                errorCount,
                actual.size()
        );

        Assertions.assertThat(actual.size())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(errorCount);

        return this;
    }
}
