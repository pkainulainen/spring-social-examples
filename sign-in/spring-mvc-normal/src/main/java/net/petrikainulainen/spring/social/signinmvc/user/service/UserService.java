package net.petrikainulainen.spring.social.signinmvc.user.service;

import net.petrikainulainen.spring.social.signinmvc.user.dto.RegistrationForm;
import net.petrikainulainen.spring.social.signinmvc.user.model.User;

/**
 * @author Petri Kainulainen
 */
public interface UserService {

    public User registerNewUserAccount(RegistrationForm dto);
}
