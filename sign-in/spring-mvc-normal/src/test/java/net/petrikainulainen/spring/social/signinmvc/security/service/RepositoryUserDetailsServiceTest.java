package net.petrikainulainen.spring.social.signinmvc.security.service;

import net.petrikainulainen.spring.social.signinmvc.security.dto.ExampleUserDetails;
import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;
import net.petrikainulainen.spring.social.signinmvc.user.model.User;
import net.petrikainulainen.spring.social.signinmvc.user.model.UserBuilder;
import net.petrikainulainen.spring.social.signinmvc.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static net.petrikainulainen.spring.social.signinmvc.security.dto.ExampleUserDetailsAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Petri Kainulainen
 */
@RunWith(MockitoJUnitRunner.class)
public class RepositoryUserDetailsServiceTest {

    private static final String EMAIL = "foo@bar.com";
    private static final String FIRST_NAME = "Foo";
    private static final Long ID = 1L;
    private static final String LAST_NAME = "Bar";
    private static final String PASSWORD = "password";

    private RepositoryUserDetailsService service;

    @Mock
    private UserRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryUserDetailsService(repositoryMock);
    }

    @Test
    public void loadByUsername_UserNotFound_ShouldThrowException() {
        when(repositoryMock.findByEmail(EMAIL)).thenReturn(null);

        catchException(service).loadUserByUsername(EMAIL);
        Assertions.assertThat(caughtException())
                .isExactlyInstanceOf(UsernameNotFoundException.class)
                .hasMessage("No user found with username: " + EMAIL)
                .hasNoCause();

        verify(repositoryMock, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void loadByUsername_UserRegisteredByUsingFormRegistration_ShouldReturnCorrectUserDetails() {
        User found = new UserBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .id(ID)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .build();

        when(repositoryMock.findByEmail(EMAIL)).thenReturn(found);

        UserDetails user = service.loadUserByUsername(EMAIL);
        ExampleUserDetails actual = (ExampleUserDetails) user;

        assertThat(actual)
                .hasFirstName(FIRST_NAME)
                .hasId(ID)
                .hasLastName(LAST_NAME)
                .hasPassword(PASSWORD)
                .hasUsername(EMAIL)
                .isActive()
                .isRegisteredUser()
                .isRegisteredByUsingFormRegistration();

        verify(repositoryMock, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void loadByUsername_UserSignedInByUsingSocialSignInProvider_ShouldReturnCorrectUserDetails() {
        User found = new UserBuilder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .id(ID)
                .lastName(LAST_NAME)
                .signInProvider(SocialMediaService.TWITTER)
                .build();

        when(repositoryMock.findByEmail(EMAIL)).thenReturn(found);

        UserDetails user = service.loadUserByUsername(EMAIL);
        ExampleUserDetails actual = (ExampleUserDetails) user;

        assertThat(actual)
                .hasFirstName(FIRST_NAME)
                .hasId(ID)
                .hasLastName(LAST_NAME)
                .hasUsername(EMAIL)
                .isActive()
                .isRegisteredUser()
                .isSignedInByUsingSocialSignInProvider(SocialMediaService.TWITTER);

        verify(repositoryMock, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(repositoryMock);
    }
}
