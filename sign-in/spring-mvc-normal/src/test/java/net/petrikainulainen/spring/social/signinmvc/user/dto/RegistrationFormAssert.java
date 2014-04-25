package net.petrikainulainen.spring.social.signinmvc.user.dto;

import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import org.fest.assertions.GenericAssert;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Petri Kainulainen
 */
public class RegistrationFormAssert extends GenericAssert<RegistrationFormAssert, RegistrationForm> {

    private RegistrationFormAssert(RegistrationForm actual) {
        super(RegistrationFormAssert.class, actual);
    }

    public static RegistrationFormAssert assertThatRegistrationForm(RegistrationForm actual) {
        return new RegistrationFormAssert(actual);
    }

    public RegistrationFormAssert hasEmail(String email) {
        isNotNull();

        String errorMessage = String.format("Expected email to be <%s> but was <%s>",
                email,
                actual.getEmail()
        );

        assertThat(actual.getEmail())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(email);

        return this;
    }

    public RegistrationFormAssert hasFirstName(String firstName) {
        isNotNull();

        String errorMessage = String.format("Expected first name to be <%s> but was <%s>",
                firstName,
                actual.getFirstName()
        );

        assertThat(actual.getFirstName())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(firstName);

        return this;
    }

    public RegistrationFormAssert hasLastName(String lastName) {
        isNotNull();

        String errorMessage = String.format("Expected last name to be <%s> but was <%s>",
                lastName,
                actual.getLastName()
        );

        assertThat(actual.getLastName())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(lastName);

        return this;
    }

    public RegistrationFormAssert hasNoPassword() {
        isNotNull();

        String errorMessage = String.format("Expected password to be <null> but was <%s>",
                actual.getPassword()
        );

        assertThat(actual.getPassword())
                .overridingErrorMessage(errorMessage)
                .isNull();

        return this;
    }

    public RegistrationFormAssert hasNoPasswordVerification() {
        isNotNull();

        String errorMessage = String.format("Expected password verification to be <null> but was <%s>",
                actual.getPasswordVerification()
        );

        assertThat(actual.getPasswordVerification())
                .overridingErrorMessage(errorMessage)
                .isNull();

        return this;
    }

    public RegistrationFormAssert hasPassword(String password) {
        isNotNull();

        String errorMessage = String.format("Expected password to be <%s> but was <%s>",
                password,
                actual.getPassword()
        );

        assertThat(actual.getPassword())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(password);

        return this;
    }

    public RegistrationFormAssert hasPasswordVerification(String passwordVerification) {
        isNotNull();

        String errorMessage = String.format("Expected password verification to be <%s> but was <%s>",
                passwordVerification,
                actual.getPasswordVerification()
        );

        assertThat(actual.getPasswordVerification())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(passwordVerification);

        return this;
    }

    public RegistrationFormAssert isNormalRegistration() {
        isNotNull();

        String errorMessage = String.format("Expected sign in provider to be <null> but was <%s>",
                actual.getSignInProvider()
        );

        assertThat(actual.getSignInProvider())
                .overridingErrorMessage(errorMessage)
                .isNull();

        return this;
    }

    public RegistrationFormAssert isSocialSignInWithSignInProvider(SocialMediaService signInProvider) {
        isNotNull();

        String errorMessage = String.format("Expected sign in provider to be <%s> but was <%s>",
                signInProvider,
                actual.getSignInProvider()
        );

        assertThat(actual.getSignInProvider())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(signInProvider);

        return this;
    }
}
