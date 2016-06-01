package net.petrikainulainen.spring.social.signinmvc.config;

import javax.sql.DataSource;

import net.petrikainulainen.spring.social.signinmvc.security.service.CustomPersistentTokenBasedRememberMeServices;
import net.petrikainulainen.spring.social.signinmvc.security.service.RepositoryUserDetailsService;
import net.petrikainulainen.spring.social.signinmvc.security.service.SimpleSocialUserDetailsService;
import net.petrikainulainen.spring.social.signinmvc.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author Petri Kainulainen
 */
@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
  	DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                //Spring Security ignores request to static resources such as CSS or JS files.
                .ignoring()
                    .antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Configures form login
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login/authenticate")
                    .failureUrl("/login?error=bad_credentials")
                //Configures the logout function
                .and()
                    .logout()
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                //Configures url based authorization
                .and()
                    .authorizeRequests()
                        //Anyone can access the urls
                        .antMatchers(
                                "/auth/**",
                                "/login",
                                "/signup/**",
                                "/user/register/**"
                        ).permitAll()
                        //The rest of the our application is protected.
                        .antMatchers("/**").hasRole("USER")
                //Adds the CustomPersistentTokenBasedRememberMeServices.
                .and()
                		.rememberMe()
                			.key("myRememberMeKey")
                			.rememberMeServices(customPersistentTokenBasedRememberMeServices())
                //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
                .and()
                    .apply(new SpringSocialConfigurer());
    }

    /**
     * Configures the authentication manager bean which processes authentication
     * requests.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    /**
     * This is used to hash the password of the user.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * This bean is used to load the user specific data when social sign in
     * is used.
     */
    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
        return new SimpleSocialUserDetailsService(userDetailsService());
    }

    /**
     * This bean is load the user specific data when form login is used.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new RepositoryUserDetailsService(userRepository);
    }
    
    /**
     * This bean is the custom persistent token-based remember me service which handles persistent remember
     * using browser cookie for both regular login and social login.
     */
    @Bean
    public CustomPersistentTokenBasedRememberMeServices customPersistentTokenBasedRememberMeServices(){
    	CustomPersistentTokenBasedRememberMeServices rememberMeServices = new CustomPersistentTokenBasedRememberMeServices("myRememberMeKey", userDetailsService(), persistentTokenRepository());
    	rememberMeServices.setParameter("rememberme");
    	rememberMeServices.setTokenValiditySeconds(1209600);
    	return rememberMeServices;
    }
    
    /**
     * This bean is the JDBC token repository for remember me services.
     */
    @Bean
  	public PersistentTokenRepository persistentTokenRepository() {
  		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
  		db.setCreateTableOnStartup(false);
  		db.setDataSource(dataSource);
  		return db;
  	}
}
