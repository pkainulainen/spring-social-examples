package net.petrikainulainen.spring.social.signinmvc.security.util;

import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import net.petrikainulainen.spring.social.signinmvc.user.model.User;
import net.petrikainulainen.spring.social.signinmvc.user.model.UserBuilder;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static net.petrikainulainen.spring.social.signinmvc.security.util.SecurityContextAssert.assertThat;

/**
 * @author Petri Kainulainen
 */
public class SecurityUtilTest {

    private static final String EMAIL = "foo@bar.com";
    private static final String FIRST_NAME = "Foo";
    private static final Long ID = 1L;
    private static final String LAST_NAME = "Bar";
    private static final String PASSWORD = "password";

    @Test
    public void logInUser_UserRegisteredByUsingFormRegistration_ShouldAddUserDetailsToSecurityContext() {
        User user = new UserBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .id(ID)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .build();

        SecurityUtil.logInUser(user);

        assertThat(SecurityContextHolder.getContext())
                .loggedInUserIs(user)
                .loggedInUserHasPassword(PASSWORD)
                .loggedInUserIsRegisteredByUsingNormalRegistration();
    }

    @Test
    public void logInUser_UserSignInByUsingSocialSignInProvider_ShouldAddUserDetailsToSecurityContext() {
        User user = new UserBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .id(ID)
                .lastName(LAST_NAME)
                .signInProvider(SocialMediaService.TWITTER)
                .build();

        SecurityUtil.logInUser(user);

        assertThat(SecurityContextHolder.getContext())
                .loggedInUserIs(user)
                .loggedInUserIsSignedInByUsingSocialProvider(SocialMediaService.TWITTER);
    }
}
