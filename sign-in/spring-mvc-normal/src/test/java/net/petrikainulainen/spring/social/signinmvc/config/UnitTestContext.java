package net.petrikainulainen.spring.social.signinmvc.config;

import net.petrikainulainen.spring.social.signinmvc.user.repository.UserRepository;
import net.petrikainulainen.spring.social.signinmvc.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

/**
 * @author Petri Kainulainen
 */
@Configuration
public class UnitTestContext {

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }
}
