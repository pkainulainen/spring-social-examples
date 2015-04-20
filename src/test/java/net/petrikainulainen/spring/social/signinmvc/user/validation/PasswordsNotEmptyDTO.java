package net.petrikainulainen.spring.social.signinmvc.user.validation;

/**
 * @author Petri Kainulainen
 */
@PasswordsNotEmpty(
        triggerFieldName = "trigger",
        passwordFieldName = "password",
        passwordVerificationFieldName = "passwordVerification"
)
public class PasswordsNotEmptyDTO {

    private String password;
    private String passwordVerification;
    private String trigger;

    public static PasswordsNotEmptyDTOBuilder getBuilder() {
        return new PasswordsNotEmptyDTOBuilder();
    }

    public static class PasswordsNotEmptyDTOBuilder {

        private PasswordsNotEmptyDTO dto;

        public PasswordsNotEmptyDTOBuilder() {
            dto = new PasswordsNotEmptyDTO();
        }

        public PasswordsNotEmptyDTOBuilder password(String password) {
            dto.password = password;
            return this;
        }

        public PasswordsNotEmptyDTOBuilder passwordVerification(String passwordVerification) {
            dto.passwordVerification = passwordVerification;
            return this;
        }

        public PasswordsNotEmptyDTOBuilder trigger(String trigger) {
            dto.trigger = trigger;
            return this;
        }

        public PasswordsNotEmptyDTO build() {
            return dto;
        }
    }

}
