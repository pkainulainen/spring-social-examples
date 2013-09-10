package net.petrikainulainen.spring.social.signinmvc.security.dto;

import net.petrikainulainen.spring.social.signinmvc.user.model.Role;
import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Petri Kainulainen
 */
public class ExampleUserDetailsAssert extends GenericAssert<ExampleUserDetailsAssert, ExampleUserDetails> {

    public ExampleUserDetailsAssert(ExampleUserDetails actual) {
        super(ExampleUserDetailsAssert.class, actual);
    }

    public static ExampleUserDetailsAssert assertThat(ExampleUserDetails actual) {
        return new ExampleUserDetailsAssert(actual);
    }

    public ExampleUserDetailsAssert hasFirstName(String firstName) {
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

    public ExampleUserDetailsAssert hasId(Long id) {
        isNotNull();

        String errorMessage = String.format(
                "Expected id to be <%d> but was <%d>",
                id,
                actual.getId()
        );

        Assertions.assertThat(actual.getId())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(id);

        return this;
    }

    public ExampleUserDetailsAssert hasLastName(String lastName) {
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

    public ExampleUserDetailsAssert hasPassword(String password) {
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

    public ExampleUserDetailsAssert hasUsername(String username) {
        isNotNull();

        String errorMessage = String.format(
                "Expected username to be <%s> but was <%s>",
                username,
                actual.getUsername()
        );

        Assertions.assertThat(actual.getUsername())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(username);

        return this;
    }

    public ExampleUserDetailsAssert isActive() {
        isNotNull();

        String expirationErrorMessage = "Expected account to be non expired but it was expired";
        Assertions.assertThat(actual.isAccountNonExpired())
                .overridingErrorMessage(expirationErrorMessage)
                .isTrue();

        String lockedErrorMessage = "Expected account to be non locked but it was locked";
        Assertions.assertThat(actual.isAccountNonLocked())
                .overridingErrorMessage(lockedErrorMessage)
                .isTrue();

        String credentialsExpirationErrorMessage = "Expected credentials to be non expired but they were expired";
        Assertions.assertThat(actual.isCredentialsNonExpired())
                .overridingErrorMessage(credentialsExpirationErrorMessage)
                .isTrue();

        String enabledErrorMessage = "Expected account to be enabled but it was not";
        Assertions.assertThat(actual.isEnabled())
                .overridingErrorMessage(enabledErrorMessage)
                .isTrue();

        return this;
    }

    public ExampleUserDetailsAssert isRegisteredUser() {
        isNotNull();

        String errorMessage = String.format(
                "Expected role to be <ROLE_USER> but was <%s>",
                actual.getRole()
        );

        Assertions.assertThat(actual.getRole())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(Role.ROLE_USER);

        Collection<? extends GrantedAuthority> authorities = actual.getAuthorities();

        String authoritiesCountMessage = String.format(
                "Expected <1> granted authority but found <%d>",
                authorities.size()
        );

        Assertions.assertThat(authorities.size())
                .overridingErrorMessage(authoritiesCountMessage)
                .isEqualTo(1);

        GrantedAuthority authority = authorities.iterator().next();

        String authorityErrorMessage = String.format(
                "Expected authority to be <ROLE_USER> but was <%s>",
                authority.getAuthority()
        );

        Assertions.assertThat(authority.getAuthority())
                .overridingErrorMessage(authorityErrorMessage)
                .isEqualTo(Role.ROLE_USER.name());

        return this;
    }

    public ExampleUserDetailsAssert isRegisteredByUsingFormRegistration() {
        isNotNull();

        String errorMessage = String.format(
                "Expected socialSignInProvider to be <null> but was <%s>",
                actual.getSocialSignInProvider()
        );

        Assertions.assertThat(actual.getSocialSignInProvider())
                .overridingErrorMessage(errorMessage)
                .isNull();

        return this;
    }

    public ExampleUserDetailsAssert isSignedInByUsingSocialSignInProvider(SocialMediaService socialSignInProvider) {
        isNotNull();

        String errorMessage = String.format(
                "Expected socialSignInProvider to be <%s> but was <%s>",
                socialSignInProvider,
                actual.getSocialSignInProvider()
        );

        Assertions.assertThat(actual.getSocialSignInProvider())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(socialSignInProvider);

        return this;
    }
}
