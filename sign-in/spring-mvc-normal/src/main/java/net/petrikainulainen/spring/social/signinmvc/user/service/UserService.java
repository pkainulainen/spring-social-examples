package net.petrikainulainen.spring.social.signinmvc.user.service;

import net.petrikainulainen.spring.social.signinmvc.user.dto.RegistrationForm;
import net.petrikainulainen.spring.social.signinmvc.user.model.User;

/**
 * @author Petri Kainulainen
 */
public interface UserService {

    /**
     * Creates a new user account to the service.
     * @param userAccountData   The information of the created user account.
     * @return  The information of the created user account.
     */
    public User registerNewUserAccount(RegistrationForm userAccountData);
}
