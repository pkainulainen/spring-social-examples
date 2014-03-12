package net.petrikainulainen.spring.social.signinmvc.user.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import net.petrikainulainen.spring.social.signinmvc.ColumnSensingFlatXMLDataSetLoader;
import net.petrikainulainen.spring.social.signinmvc.IntegrationTestConstants;
import net.petrikainulainen.spring.social.signinmvc.TestUtil;
import net.petrikainulainen.spring.social.signinmvc.config.ExampleApplicationContext;
import net.petrikainulainen.spring.social.signinmvc.config.IntegrationTestContext;
import net.petrikainulainen.spring.social.signinmvc.user.dto.RegistrationForm;
import net.petrikainulainen.spring.social.signinmvc.user.dto.RegistrationFormBuilder;
import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.TestProviderSignInAttemptBuilder;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.TestProviderSignInAttempt;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static net.petrikainulainen.spring.social.signinmvc.user.controller.TestProviderSignInAttemptAssert.assertThatSignIn;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Petri Kainulainen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExampleApplicationContext.class, IntegrationTestContext.class})
//@ContextConfiguration(locations = {"classpath:exampleApplicationContext.xml"})
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ColumnSensingFlatXMLDataSetLoader.class)
public class ITRegistrationControllerTest {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String DISPLAY_NAME = "John Smith";
    private static final String EMAIL = "john.smith@gmail.com";
    private static final Long EXPIRE_TIME = 100000L;
    private static final String IMAGE_URL = "https://www.twitter.com/images/johnsmith.jpg";
    private static final String MALFORMED_EMAIL = "john.smithatgmail.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_VERIFICATION = "passwordVerification";
    private static final String PROFILE_URL = "https://www.twitter.com/johnsmith";
    private static final String PROVIDER_USER_ID = "johnsmith";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String SECRET = "secret";
    private static final SocialMediaService SIGN_IN_PROVIDER = SocialMediaService.TWITTER;
    private static final String SOCIAL_MEDIA_SERVICE = "twitter";

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
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
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showRegistrationForm_SocialSignInWithAllValues_ShouldRenderRegistrationPageWithAllValuesSet() throws Exception {
        TestProviderSignInAttempt socialSignIn = new TestProviderSignInAttemptBuilder()
                .connectionData()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

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
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showRegistrationForm_SocialSignInWithNoValues_ShouldRenderRegistrationPageWithProviderDetails() throws Exception {
        TestProviderSignInAttempt socialSignIn = new TestProviderSignInAttemptBuilder()
                .connectionData()
                .providerId(SOCIAL_MEDIA_SERVICE)
                .userProfile()
                .build();

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
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_NormalRegistrationAndEmptyForm_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder().build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
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
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
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

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
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
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_NormalRegistrationAndPasswordMismatch_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD_VERIFICATION)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
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
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/social/signinmvc/user/users.xml")
    @ExpectedDatabase(value = "/net/petrikainulainen/spring/social/signinmvc/user/users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_NormalRegistrationAndEmailExists_ShouldRenderRegistrationFormWithFieldError() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(IntegrationTestConstants.User.REGISTERED_USER.getUsername())
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(IntegrationTestConstants.User.REGISTERED_USER.getUsername())),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", is(PASSWORD)),
                        hasProperty("passwordVerification", is(PASSWORD)),
                        hasProperty("signInProvider", isEmptyOrNullString())
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_NormalRegistrationAndMalformedEmail_ShouldRenderRegistrationFormWithValidationError() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(MALFORMED_EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
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
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value = "register-normal-user-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_NormalRegistration_ShouldCreateNewUserAccountAndRenderHomePage() throws Exception {
        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .passwordVerification(PASSWORD)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_SocialSignInAndEmptyForm_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        TestProviderSignInAttempt socialSignIn = new TestProviderSignInAttemptBuilder()
                .connectionData()
                    .accessToken(ACCESS_TOKEN)
                    .displayName(DISPLAY_NAME)
                    .expireTime(EXPIRE_TIME)
                    .imageUrl(IMAGE_URL)
                    .profileUrl(PROFILE_URL)
                    .providerId(SOCIAL_MEDIA_SERVICE)
                    .providerUserId(PROVIDER_USER_ID)
                    .refreshToken(REFRESH_TOKEN)
                    .secret(SECRET)
                .usersConnectionRepository(usersConnectionRepository)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
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
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_SocialSignInAndTooLongValues_ShouldRenderRegistrationFormWithValidationErrors() throws Exception {
        TestProviderSignInAttempt socialSignIn = new TestProviderSignInAttemptBuilder()
                .connectionData()
                    .accessToken(ACCESS_TOKEN)
                    .displayName(DISPLAY_NAME)
                    .expireTime(EXPIRE_TIME)
                    .imageUrl(IMAGE_URL)
                    .profileUrl(PROFILE_URL)
                    .providerId(SOCIAL_MEDIA_SERVICE)
                    .providerUserId(PROVIDER_USER_ID)
                    .refreshToken(REFRESH_TOKEN)
                    .secret(SECRET)
                .usersConnectionRepository(usersConnectionRepository)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        String email = TestUtil.createStringWithLength(101);
        String firstName = TestUtil.createStringWithLength(101);
        String lastName = TestUtil.createStringWithLength(101);

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
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
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="no-users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_SocialSignInAndMalformedEmail_ShouldRenderRegistrationFormWithValidationError() throws Exception {
        TestProviderSignInAttempt socialSignIn = new TestProviderSignInAttemptBuilder()
                .connectionData()
                    .accessToken(ACCESS_TOKEN)
                    .displayName(DISPLAY_NAME)
                    .expireTime(EXPIRE_TIME)
                    .imageUrl(IMAGE_URL)
                    .profileUrl(PROFILE_URL)
                    .providerId(SOCIAL_MEDIA_SERVICE)
                    .providerUserId(PROVIDER_USER_ID)
                    .refreshToken(REFRESH_TOKEN)
                    .secret(SECRET)
                .usersConnectionRepository(usersConnectionRepository)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(MALFORMED_EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
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
    }

    @Test
    @DatabaseSetup("/net/petrikainulainen/spring/social/signinmvc/user/users.xml")
    @ExpectedDatabase(value = "/net/petrikainulainen/spring/social/signinmvc/user/users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_SocialSignInAndEmailExist_ShouldRenderRegistrationFormWithFieldError() throws Exception {
        TestProviderSignInAttempt socialSignIn = new TestProviderSignInAttemptBuilder()
                .connectionData()
                    .accessToken(ACCESS_TOKEN)
                    .displayName(DISPLAY_NAME)
                    .expireTime(EXPIRE_TIME)
                    .imageUrl(IMAGE_URL)
                    .profileUrl(PROFILE_URL)
                    .providerId(SOCIAL_MEDIA_SERVICE)
                    .providerUserId(PROVIDER_USER_ID)
                    .refreshToken(REFRESH_TOKEN)
                    .secret(SECRET)
                .usersConnectionRepository(usersConnectionRepository)
                .userProfile()
                    .email(IntegrationTestConstants.User.REGISTERED_USER.getUsername())
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(IntegrationTestConstants.User.REGISTERED_USER.getUsername())
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/registrationForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/registrationForm.jsp"))
                .andExpect(model().attribute("user", allOf(
                        hasProperty("email", is(IntegrationTestConstants.User.REGISTERED_USER.getUsername())),
                        hasProperty("firstName", is(FIRST_NAME)),
                        hasProperty("lastName", is(LAST_NAME)),
                        hasProperty("password", isEmptyOrNullString()),
                        hasProperty("passwordVerification", isEmptyOrNullString()),
                        hasProperty("signInProvider", is(SIGN_IN_PROVIDER))
                )))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    @DatabaseSetup("no-users.xml")
    @ExpectedDatabase(value="register-social-user-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void registerUserAccount_SocialSignIn_ShouldCreateNewUserAccountAndRenderHomePage() throws Exception {
        TestProviderSignInAttempt socialSignIn = new TestProviderSignInAttemptBuilder()
                .connectionData()
                    .accessToken(ACCESS_TOKEN)
                    .displayName(DISPLAY_NAME)
                    .expireTime(EXPIRE_TIME)
                    .imageUrl(IMAGE_URL)
                    .profileUrl(PROFILE_URL)
                    .providerId(SOCIAL_MEDIA_SERVICE)
                    .providerUserId(PROVIDER_USER_ID)
                    .refreshToken(REFRESH_TOKEN)
                    .secret(SECRET)
                .usersConnectionRepository(usersConnectionRepository)
                .userProfile()
                    .email(EMAIL)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                .build();

        RegistrationForm userAccountData = new RegistrationFormBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .signInProvider(SIGN_IN_PROVIDER)
                .build();

        CsrfToken csrfToken = new DefaultCsrfToken(IntegrationTestConstants.CSRF_TOKEN_HEADER_NAME,
                IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME,
                IntegrationTestConstants.CSRF_TOKEN_VALUE);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(TestUtil.convertObjectToFormUrlEncodedBytes(userAccountData))
                .sessionAttr(ProviderSignInAttempt.SESSION_ATTRIBUTE, socialSignIn)
                .param(IntegrationTestConstants.CSRF_TOKEN_REQUEST_PARAM_NAME, IntegrationTestConstants.CSRF_TOKEN_VALUE)
                .sessionAttr(IntegrationTestConstants.CSRF_TOKEN_SESSION_ATTRIBUTE_NAME, csrfToken)
                .sessionAttr("user", userAccountData)
        )
                .andExpect(status().isMovedTemporarily())
                .andExpect(redirectedUrl("/"));
    }
}
