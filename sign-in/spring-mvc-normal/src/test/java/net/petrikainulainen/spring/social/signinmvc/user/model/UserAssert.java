package net.petrikainulainen.spring.social.signinmvc.user.model;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

/**
 * @author Petri Kainulainen
 */
public class UserAssert extends GenericAssert<UserAssert, User> {

    protected UserAssert(User actual) {
        super(UserAssert.class, actual);
    }

    public static UserAssert assertThat(User actual) {
        return new UserAssert(actual);
    }

    public UserAssert hasEmail(String email) {
        isNotNull();

        String errorMessage = String.format(
                "Expected email to be <%s> but was <%s>",
                email,
                actual.getEmail()
        );

        Assertions.assertThat(actual.getEmail())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(email);

        return this;
    }

    public UserAssert hasFirstName(String firstName) {
        isNotNull();

        String errorMessage = String.format(
                "Expected first name to be <%s> but was <%s>",
                firstName,
                actual.getFirstName()
        );

        Assertions.assertThat(actual.getFirstName())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(firstName);

        return this;
    }

    public UserAssert hasLastName(String lastName) {
        isNotNull();

        String errorMessage = String.format(
                "Expected last name to be <%s> but was <%s>",
                lastName,
                actual.getLastName()
        );

        Assertions.assertThat(actual.getLastName())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(lastName);

        return this;
    }

    public UserAssert hasNoId() {
        isNotNull();

        String errorMessage = String.format(
                "Expected id to be <null> but was <%d>",
                actual.getId()
        );

        Assertions.assertThat(actual.getId())
                .overridingErrorMessage(errorMessage)
                .isNull();

        return this;
    }

    public UserAssert hasNoPassword() {
        isNotNull();

        String errorMessage = String.format(
                "Expected password to be <null> but was <%s>",
                actual.getPassword()
        );

        Assertions.assertThat(actual.getPassword())
                .overridingErrorMessage(errorMessage)
                .isNull();

        return this;
    }

    public UserAssert hasPassword(String password) {
        isNotNull();

        String errorMessage = String.format(
                "Expected password to be <%s> but was <%s>",
                password,
                actual.getPassword()
        );

        Assertions.assertThat(actual.getPassword())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(password);

        return this;
    }

    public UserAssert isRegisteredByUsingNormalRegistration() {
        isNotNull();

        String errorMessage = String.format(
                "Expected signInProvider to be <null> but was <%s>",
                actual.getSignInProvider()
        );

        Assertions.assertThat(actual.getSignInProvider())
                .overridingErrorMessage(errorMessage)
                .isNull();

        return this;
    }


    public UserAssert isRegisteredByUsingSignInProvider(SocialMediaService signInProvider) {
        isNotNull();

        String errorMessage = String.format(
                "Expected signInProvider to be <%s> but was <%s>",
                signInProvider,
                actual.getSignInProvider()
        );

        Assertions.assertThat(actual.getSignInProvider())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(signInProvider);

        return this;
    }

    public UserAssert isRegisteredUser() {
        isNotNull();

        String errorMessage = String.format(
                "Expected role to be <ROLE_USER> but was <%s>",
                actual.getRole()
        );

        Assertions.assertThat(actual.getRole())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(Role.ROLE_USER);

        return this;
    }
}
