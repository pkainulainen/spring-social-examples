package org.springframework.social.connect.support;

import org.springframework.social.connect.*;
import org.springframework.social.connect.web.TestProviderSignInAttempt;

/**
 * @author Petri Kainulainen
 */
public class TestProviderSignInAttemptBuilder {

    private String accessToken;

    private String displayName;

    private String email;

    private Long expireTime;

    private String firstName;

    private String imageUrl;

    private String lastName;

    private String profileUrl;

    private String providerId;

    private String providerUserId;

    private String refreshToken;

    private String secret;

    private UsersConnectionRepository usersConnectionRepository;

    public TestProviderSignInAttemptBuilder() {

    }

    public TestProviderSignInAttemptBuilder accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public TestProviderSignInAttemptBuilder connectionData() {
        return this;
    }

    public TestProviderSignInAttemptBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public TestProviderSignInAttemptBuilder email(String email) {
        this.email = email;
        return this;
    }

    public TestProviderSignInAttemptBuilder expireTime(Long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public TestProviderSignInAttemptBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public TestProviderSignInAttemptBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public TestProviderSignInAttemptBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TestProviderSignInAttemptBuilder profileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
        return this;
    }

    public TestProviderSignInAttemptBuilder providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public TestProviderSignInAttemptBuilder providerUserId(String providerUserId) {
        this.providerUserId = providerUserId;
        return this;
    }

    public TestProviderSignInAttemptBuilder refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public TestProviderSignInAttemptBuilder secret(String secret) {
        this.secret = secret;
        return this;
    }

    public TestProviderSignInAttemptBuilder usersConnectionRepository(UsersConnectionRepository usersConnectionRepository) {
        this.usersConnectionRepository = usersConnectionRepository;
        return this;
    }

    public TestProviderSignInAttemptBuilder userProfile() {
        return this;
    }

    public TestProviderSignInAttempt build() {
        ConnectionData connectionData = new ConnectionData(providerId,
                providerUserId,
                displayName,
                profileUrl,
                imageUrl,
                accessToken,
                secret,
                refreshToken,
                expireTime);

        UserProfile userProfile = new UserProfileBuilder()
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();

        Connection connection = new TestConnection(connectionData, userProfile);

        return new TestProviderSignInAttempt(connection, usersConnectionRepository);
    }
}
