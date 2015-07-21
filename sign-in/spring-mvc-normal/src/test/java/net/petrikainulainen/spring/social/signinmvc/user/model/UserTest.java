package net.petrikainulainen.spring.social.signinmvc.user.model;

import org.junit.Test;

import static net.petrikainulainen.spring.social.signinmvc.user.model.UserAssert.assertThat;

/**
 * @author Petri Kainulainen
 */
public class UserTest {

    private static final String EMAIL = "john.smith@gmail.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String PASSWORD = "password";
    private static final SocialMediaService SIGN_IN_PROVIDER = SocialMediaService.TWITTER;

    @Test
      public void build_SignedInByUsingSocialMediaService_ShouldCreateUser() {
        User user = User.getBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        assertThat(user)
                .hasNoId()
                .hasEmail(EMAIL)
                .hasFirstName(FIRST_NAME)
                .hasLastName(LAST_NAME)
                .isRegisteredUser()
                .isRegisteredByUsingSignInProvider(SIGN_IN_PROVIDER);
    }

    @Test
    public void build_RegisteredViaNormalRegistration_ShouldCreateUser() {
        User user = User.getBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .build();

        assertThat(user)
                .hasNoId()
                .hasEmail(EMAIL)
                .hasFirstName(FIRST_NAME)
                .hasLastName(LAST_NAME)
                .hasPassword(PASSWORD)
                .isRegisteredUser()
                .isRegisteredByUsingNormalRegistration();
    }

}
