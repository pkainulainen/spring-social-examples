package net.petrikainulainen.spring.social.signinmvc.user.dto;

import net.petrikainulainen.spring.social.signinmvc.user.model.SocialMediaService;

/**
 * @author Petri Kainulainen
 */
public class RegistrationFormBuilder {

    private RegistrationForm dto;

    public RegistrationFormBuilder() {
        dto = new RegistrationForm();
    }

    public RegistrationFormBuilder email(String email) {
        dto.setEmail(email);
        return this;
    }

    public RegistrationFormBuilder firstName(String firstName) {
        dto.setFirstName(firstName);
        return this;
    }

    public RegistrationFormBuilder lastName(String lastName) {
        dto.setLastName(lastName);
        return this;
    }

    public RegistrationFormBuilder password(String password) {
        dto.setPassword(password);
        return this;
    }

    public RegistrationFormBuilder passwordVerification(String passwordVerification) {
        dto.setPasswordVerification(passwordVerification);
        return this;
    }

    public RegistrationFormBuilder isSocialSignInViaSignInProvider(SocialMediaService signInProvider) {
        dto.setSignInProvider(signInProvider);
        return this;
    }

    public RegistrationForm build() {
        return dto;
    }
}
