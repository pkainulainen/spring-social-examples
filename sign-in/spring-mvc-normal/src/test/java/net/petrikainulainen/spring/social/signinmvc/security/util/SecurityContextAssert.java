package net.petrikainulainen.spring.social.signinmvc.security.util;

import net.petrikainulainen.spring.social.signinmvc.security.dto.ExampleUserDetails;
import net.petrikainulainen.spring.social.signinmvc.security.dto.ExampleUserDetailsAssert;
import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import net.petrikainulainen.spring.social.signinmvc.user.model.User;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.springframework.security.core.context.SecurityContext;

/**
 * @author Petri Kainulainen
 */
public class SecurityContextAssert extends GenericAssert<SecurityContextAssert, SecurityContext> {

    public SecurityContextAssert(SecurityContext actual) {
        super(SecurityContextAssert.class, actual);
    }

    public static SecurityContextAssert assertThat(SecurityContext actual) {
        return new SecurityContextAssert(actual);
    }

    public SecurityContextAssert loggedInUserIs(User user) {
        isNotNull();

        ExampleUserDetails loggedIn = (ExampleUserDetails) actual.getAuthentication().getPrincipal();

        String errorMessage = String.format("Expected logged in user to be <%s> but was <null>", user);
        Assertions.assertThat(loggedIn)
                .overridingErrorMessage(errorMessage)
                .isNotNull();

        ExampleUserDetailsAssert.assertThat(loggedIn)
                .hasFirstName(user.getFirstName())
                .hasId(user.getId())
                .hasLastName(user.getLastName())
                .hasUsername(user.getEmail())
                .isActive()
                .isRegisteredUser();

        return this;
    }

    public SecurityContextAssert loggedInUserHasPassword(String password) {
        isNotNull();

        ExampleUserDetails loggedIn = (ExampleUserDetails) actual.getAuthentication().getPrincipal();

        String errorMessage = String.format("Expected logged in user to be <not null> but was <null>");
        Assertions.assertThat(loggedIn)
                .overridingErrorMessage(errorMessage)
                .isNotNull();

        ExampleUserDetailsAssert.assertThat(loggedIn)
                .hasPassword(password);

        return this;
    }

    public SecurityContextAssert loggedInUserIsRegisteredByUsingNormalRegistration() {
        isNotNull();

        ExampleUserDetails loggedIn = (ExampleUserDetails) actual.getAuthentication().getPrincipal();

        String errorMessage = String.format("Expected logged in user to be <not null> but was <null>");
        Assertions.assertThat(loggedIn)
                .overridingErrorMessage(errorMessage)
                .isNotNull();

        ExampleUserDetailsAssert.assertThat(loggedIn)
                .isRegisteredByUsingFormRegistration();

        return this;
    }

    public SecurityContextAssert loggedInUserIsSignedInByUsingSocialProvider(SocialMediaService signInProvider) {
        isNotNull();

        ExampleUserDetails loggedIn = (ExampleUserDetails) actual.getAuthentication().getPrincipal();

        String errorMessage = String.format("Expected logged in user to be <not null> but was <null>");
        Assertions.assertThat(loggedIn)
                .overridingErrorMessage(errorMessage)
                .isNotNull();

        ExampleUserDetailsAssert.assertThat(loggedIn)
                .hasPassword("SocialUser")
                .isSignedInByUsingSocialSignInProvider(signInProvider);

        return this;
    }
}
