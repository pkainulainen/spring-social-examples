package net.petrikainulainen.spring.social.signinmvc.user.controller;

import net.petrikainulainen.spring.social.signinmvc.TestUtil;
import net.petrikainulainen.spring.social.signinmvc.config.UnitTestContext;
import net.petrikainulainen.spring.social.signinmvc.config.WebAppContext;
import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import net.petrikainulainen.spring.social.signinmvc.user.service.DuplicateEmailException;
import org.springframework.social.connect.web.ProviderSignInAttemptStub;
import org.springframework.social.connect.support.TestConnection;
import org.springframework.social.connect.support.TestConnectionBuilder;
import net.petrikainulainen.spring.social.signinmvc.user.dto.RegistrationForm;
import net.petrikainulainen.spring.social.signinmvc.user.dto.RegistrationFormBuilder;
import net.petrikainulainen.spring.social.signinmvc.user.model.User;
import net.petrikainulainen.spring.social.signinmvc.user.model.UserBuilder;
import net.petrikainulainen.spring.social.signinmvc.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static net.petrikainulainen.spring.social.signinmvc.security.util.SecurityContextAssert.assertThat;
import static net.petrikainulainen.spring.social.signinmvc.user.controller.ProviderSignInAttemptStubAssert.assertThatSignIn;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebAppContext.class, UnitTestContext.class})
//@ContextConfiguration(locations = {"classpath:unitTestContext.xml", "classpath:exampleApplicationContext-web.xml"})
@WebAppConfiguration
public class RegistrationControllerTest {

    private static final String EMAIL = "john.smith@gmail.com";
    private static final String MALFORMED_EMAIL = "john.smithatgmail.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_VERIFICATION = "passwordVerification";
    private static final SocialMediaService SIGN_IN_PROVIDER = SocialMediaService.TWITTER;
    private static final String SOCIAL_MEDIA_SERVICE = "twitter";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private UserService userServiceMock;

    @Before
    public void setUp() {
        Mockito.reset(userServiceMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build();
    }

    @Test
    public void showRegistrationForm_NormalRegistration_ShouldRenderRegistrationPageWithEmptyForm() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", isEmptyOrNullString()),
                        hasProperty("firstName", isEmptyOrNullString()),
                        hasProperty("lastName", isEmptyOrNullString()),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", isEmptyOrNullString())
                )));

        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void showRegistrationForm_SocialSignUpWithAllValues_ShouldRenderRegistrationPageWithAllValuesSet() throws Exception {
        TestConnection socialConnection = new TestConnectionBuilder()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        ProviderSignInAttempt socialSignIn = new ProviderSignInAttemptStub(socialConnection);

        mockMvc.perform(get("/user/register")
            .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(EMAIL)),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", is(SIGN_IN_PROVIDER))
                )));
    }

    @Test
    public void showRegistrationForm_SocialSignUpWithNoValues_ShouldRenderRegistrationPageWithProviderDetails() throws Exception {
        TestConnection socialConnection = new TestConnectionBuilder()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                .build();

        ProviderSignInAttempt socialSignIn = new ProviderSignInAttemptStub(socialConnection);

        mockMvc.perform(get("/user/register")
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", isEmptyOrNullString()),
                        hasProperty("firstName", isEmptyOrNullString()),
                        hasProperty("lastName", isEmptyOrNullString()),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", is(SIGN_IN_PROVIDER))
                )));
    }

    @Test
    public void registerUserAccount_NormalRegistrationAndEmptyForm_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder().build();

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", isEmptyOrNullString()),
                        hasProperty("firstName", isEmptyOrNullString()),
                        hasProperty("lastName", isEmptyOrNullString()),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", isEmptyOrNullString())
                )))
                .andExpect(model().attributeHasFieldErrors(
                        "user",
                        "email",
                        "firstName",
                        "lastName",
                        "password",
                        "passwordVerification"
                ));

        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_NormalRegistrationAndTooLongValues_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        String email = TestUtil.createStringWithLength(101);
        String firstName = TestUtil.createStringWithLength(101);
        String lastName = TestUtil.createStringWithLength(101);

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(email)),
                        hasProperty("firstName", is(firstName)),
                        hasProperty("lastName", is(lastName)),
                        hasProperty("password", is(PASSWORD)),
                        hasProperty("passwordVerification", is(PASSWORD)),
                        hasProperty("signInProvider", isEmptyOrNullString())
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email", "firstName", "lastName"));

        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_NormalRegistrationAndPasswordMismatch_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD_VERIFICATION)
                .build();

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(EMAIL)),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", is(PASSWORD)),
                        hasProperty("passwordVerification", is(PASSWORD_VERIFICATION)),
                        hasProperty("signInProvider", isEmptyOrNullString())
                )))
                .andExpect(model().attributeHasFieldErrors("user", "password", "passwordVerification"));

        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_NormalRegistrationAndEmailExists_ShouldRenderRegistrationFormWithFieldError() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        when(userServiceMock.registerNewUserAccount(userAccountData)).thenThrow(new DuplicateEmailException(""));

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(EMAIL)),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", is(PASSWORD)),
                        hasProperty("passwordVerification", is(PASSWORD)),
                        hasProperty("signInProvider", isEmptyOrNullString())
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        verify(userServiceMock, times(1)).registerNewUserAccount(userAccountData);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_NormalRegistrationAndMalformedEmail_ShouldRenderRegistrationFormWithValidationError() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(MALFORMED_EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(MALFORMED_EMAIL)),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", is(PASSWORD)),
                        hasProperty("passwordVerification", is(PASSWORD)),
                        hasProperty("signInProvider", isEmptyOrNullString())
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_NormalRegistration_ShouldCreateNewUserAccountAndRenderHomePage() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        User registered = new UserBuilder()
                .id(1L)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .build();

        when(userServiceMock.registerNewUserAccount(userAccountData)).thenReturn(registered);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/"));

        assertThat(SecurityContextHolder.getContext())
                .loggedInUserIs(registered)
                .loggedInUserHasPassword(registered.getPassword())
                .loggedInUserIsRegisteredByUsingNormalRegistration();

        verify(userServiceMock, times(1)).registerNewUserAccount(userAccountData);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_SignUpViaSocialProviderAndEmptyForm_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        TestConnection socialConnection = new TestConnectionBuilder()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        ProviderSignInAttemptStub socialSignIn = new ProviderSignInAttemptStub(socialConnection);

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", isEmptyOrNullString()),
                        hasProperty("firstName", isEmptyOrNullString()),
                        hasProperty("lastName", isEmptyOrNullString()),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", is(SIGN_IN_PROVIDER))
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email", "firstName", "lastName"));

        assertThatSignIn(socialSignIn).createdNoConnections();
        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_SocialSignInAndTooLongValues_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        TestConnection socialConnection = new TestConnectionBuilder()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        ProviderSignInAttemptStub socialSignIn = new ProviderSignInAttemptStub(socialConnection);

        String email = TestUtil.createStringWithLength(101);
        String firstName = TestUtil.createStringWithLength(101);
        String lastName = TestUtil.createStringWithLength(101);

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(email)),
                        hasProperty("firstName", is(firstName)),
                        hasProperty("lastName", is(lastName)),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", is(SIGN_IN_PROVIDER))
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email", "firstName", "lastName"));

        assertThatSignIn(socialSignIn).createdNoConnections();
        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_SocialSignInAndMalformedEmail_ShouldRenderRegistrationFormWithValidationError() throws Exception {
        TestConnection socialConnection = new TestConnectionBuilder()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        ProviderSignInAttemptStub socialSignIn = new ProviderSignInAttemptStub(socialConnection);

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(MALFORMED_EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(MALFORMED_EMAIL)),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", is(SIGN_IN_PROVIDER))
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        assertThatSignIn(socialSignIn).createdNoConnections();
        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_SocialSignInAndEmailExist_ShouldRenderRegistrationFormWithFieldError() throws Exception {
        TestConnection socialConnection = new TestConnectionBuilder()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        ProviderSignInAttemptStub socialSignIn = new ProviderSignInAttemptStub(socialConnection);

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        when(userServiceMock.registerNewUserAccount(userAccountData)).thenThrow(new DuplicateEmailException(""));

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(EMAIL)),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", is(SIGN_IN_PROVIDER))
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        assertThatSignIn(socialSignIn).createdNoConnections();

        verify(userServiceMock, times(1)).registerNewUserAccount(userAccountData);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void registerUserAccount_SocialSignIn_ShouldCreateNewUserAccountAndRenderHomePage() throws Exception {
        TestConnection socialConnection = new TestConnectionBuilder()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        ProviderSignInAttemptStub socialSignIn = new ProviderSignInAttemptStub(socialConnection);

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        User registered = new UserBuilder()
                .id(1L)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        when(userServiceMock.registerNewUserAccount(userAccountData)).thenReturn(registered);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/"));

        assertThat(SecurityContextHolder.getContext())
                .loggedInUserIs(registered)
                .loggedInUserIsSignedInByUsingSocialProvider(SIGN_IN_PROVIDER);
        assertThatSignIn(socialSignIn).createdConnectionForUserId(EMAIL);

        verify(userServiceMock, times(1)).registerNewUserAccount(userAccountData);
        verifyNoMoreInteractions(userServiceMock);
    }
}
