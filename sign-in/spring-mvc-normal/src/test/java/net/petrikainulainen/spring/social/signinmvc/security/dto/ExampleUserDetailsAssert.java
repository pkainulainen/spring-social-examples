package net.petrikainulainen.spring.social.signinmvc.security.dto;

import net.petrikainulainen.spring.social.signinmvc.user.model.Role;
import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Petri Kainulainen
 */
public class ExampleUserDetailsAssert extends AbstractAssert<ExampleUserDetailsAssert, ExampleUserDetails> {

    private ExampleUserDetailsAssert(ExampleUserDetails actual) {
        super(actual, ExampleUserDetailsAssert.class);
    }

    public static ExampleUserDetailsAssert assertThat(ExampleUserDetails actual) {
        return new ExampleUserDetailsAssert(actual);
    }

    public ExampleUserDetailsAssert hasFirstName(String firstName) {
        isNotNull();

        Assertions.assertThat(actual.getFirstName())
                .overridingErrorMessage("Expected first name to be <%s> but was <%s>",
                        firstName,
                        actual.getFirstName()
                )
                .isEqualTo(firstName);

        return this;
    }

    public ExampleUserDetailsAssert hasId(Long id) {
        isNotNull();

        Assertions.assertThat(actual.getId())
                .overridingErrorMessage( "Expected id to be <%d> but was <%d>",
                        id,
                        actual.getId()
                )
                .isEqualTo(id);

        return this;
    }

    public ExampleUserDetailsAssert hasLastName(String lastName) {
        isNotNull();

        Assertions.assertThat(actual.getLastName())
                .overridingErrorMessage("Expected last name to be <%s> but was <%s>",
                        lastName,
                        actual.getLastName()
                )
                .isEqualTo(lastName);

        return this;
    }

    public ExampleUserDetailsAssert hasPassword(String password) {
        isNotNull();

        Assertions.assertThat(actual.getPassword())
                .overridingErrorMessage("Expected password to be <%s> but was <%s>",
                        password,
                        actual.getPassword()
                )
                .isEqualTo(password);

        return this;
    }

    public ExampleUserDetailsAssert hasUsername(String username) {
        isNotNull();

        Assertions.assertThat(actual.getUsername())
                .overridingErrorMessage("Expected username to be <%s> but was <%s>",
                        username,
                        actual.getUsername()
                )
                .isEqualTo(username);

        return this;
    }

    public ExampleUserDetailsAssert isActive() {
        isNotNull();

        Assertions.assertThat(actual.isAccountNonExpired())
                .overridingErrorMessage("Expected account to be non expired but it was expired")
                .isTrue();

        Assertions.assertThat(actual.isAccountNonLocked())
                .overridingErrorMessage("Expected account to be non locked but it was locked")
                .isTrue();

        Assertions.assertThat(actual.isCredentialsNonExpired())
                .overridingErrorMessage("Expected credentials to be non expired but they were expired")
                .isTrue();

        Assertions.assertThat(actual.isEnabled())
                .overridingErrorMessage("Expected account to be enabled but it was not")
                .isTrue();

        return this;
    }

    public ExampleUserDetailsAssert isRegisteredUser() {
        isNotNull();

        Assertions.assertThat(actual.getRole())
                .overridingErrorMessage( "Expected role to be <ROLE_USER> but was <%s>",
                        actual.getRole()
                )
                .isEqualTo(Role.ROLE_USER);

        Collection<? extends GrantedAuthority> authorities = actual.getAuthorities();

        Assertions.assertThat(authorities.size())
                .overridingErrorMessage( "Expected <1> granted authority but found <%d>",
                        authorities.size()
                )
                .isEqualTo(1);

        GrantedAuthority authority = authorities.iterator().next();

        Assertions.assertThat(authority.getAuthority())
                .overridingErrorMessage( "Expected authority to be <ROLE_USER> but was <%s>",
                        authority.getAuthority()
                )
                .isEqualTo(Role.ROLE_USER.name());

        return this;
    }

    public ExampleUserDetailsAssert isRegisteredByUsingFormRegistration() {
        isNotNull();

        Assertions.assertThat(actual.getSocialSignInProvider())
                .overridingErrorMessage( "Expected socialSignInProvider to be <null> but was <%s>",
                        actual.getSocialSignInProvider()
                )
                .isNull();

        return this;
    }

    public ExampleUserDetailsAssert isSignedInByUsingSocialSignInProvider(SocialMediaService socialSignInProvider) {
        isNotNull();

        Assertions.assertThat(actual.getSocialSignInProvider())
                .overridingErrorMessage( "Expected socialSignInProvider to be <%s> but was <%s>",
                        socialSignInProvider,
                        actual.getSocialSignInProvider()
                )
                .isEqualTo(socialSignInProvider);

        return this;
    }
}
