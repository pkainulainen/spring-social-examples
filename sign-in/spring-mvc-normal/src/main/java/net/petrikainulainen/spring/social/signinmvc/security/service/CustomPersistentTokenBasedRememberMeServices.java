package net.petrikainulainen.spring.social.signinmvc.security.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * This class handles rememberMeRequested decision only.
 * This rememberMeRequested returns original results for regular(id/password) login but returns always 'true' for social login.
 * @author Hosang Jeon
 */
public class CustomPersistentTokenBasedRememberMeServices extends
		PersistentTokenBasedRememberMeServices {

	public CustomPersistentTokenBasedRememberMeServices(String key,
			UserDetailsService userDetailsService,
			PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
	}

	@Override
	protected boolean rememberMeRequested(HttpServletRequest request,
			String parameter) {
		
		String isRegularLogin = request.getParameter("isRegularLogin");

				// Regular Login
        if (isRegularLogin != null && "true".equals(isRegularLogin)) {
        	return super.rememberMeRequested(request, parameter);
        }
        // Social Login 
        else{
        	// returns always 'true' for social login.
        	return true;
        }
	}
}
