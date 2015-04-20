package net.petrikainulainen.spring.social.signinmvc.user.service;

/**
 * The exception is thrown when the email given during the registration
 * phase is already found from the database.
 * @author Petri Kainulainen
 */
public class DuplicateEmailException extends Exception {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
