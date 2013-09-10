package net.petrikainulainen.spring.social.signinmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.social.config.annotation.EnableJdbcConnectionRepository;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.config.annotation.EnableFacebook;
import org.springframework.social.twitter.config.annotation.EnableTwitter;

/**
 * @author Petri Kainulainen
 */
@Configuration
@EnableJdbcConnectionRepository
@EnableFacebook(appId = "${facebook.app.id}", appSecret = "${facebook.app.secret}")
@EnableTwitter(appId = "${twitter.consumer.key}", appSecret = "${twitter.consumer.secret}")
@Profile("application")
public class SocialContext {

    /**
     * This bean manages the connection flow between the account provider and
     * the example application.
     */
    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }
}
