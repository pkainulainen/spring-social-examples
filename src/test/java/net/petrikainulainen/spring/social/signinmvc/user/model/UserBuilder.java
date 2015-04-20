package net.petrikainulainen.spring.social.signinmvc.user.model;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Petri Kainulainen
 */
public class UserBuilder {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private SocialMediaService signInProvider;

    public UserBuilder() {

    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder signInProvider(SocialMediaService signInProvider) {
        this.signInProvider = signInProvider;
        return this;
    }

    public User build() {
        User user = User.getBuilder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .signInProvider(signInProvider)
                .build();

        ReflectionTestUtils.setField(user, "id", id);

        return user;
    }
}
