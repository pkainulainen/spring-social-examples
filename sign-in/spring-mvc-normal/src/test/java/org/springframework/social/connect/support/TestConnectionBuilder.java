package org.springframework.social.connect.support;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

/**
 * @author Petri Kainulainen
 */
public class TestConnectionBuilder {

    private String displayName;

    private String email;

    private String firstName;

    private String imageUrl;

    private String lastName;

    private String profileUrl;

    private String providerId;

    private String providerUserId;

    private UserProfileBuilder userProfileBuilder;

    public TestConnectionBuilder() {

    }

    public TestConnectionBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public TestConnectionBuilder email(String email) {
        this.email = email;
        return this;
    }

    public TestConnectionBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public TestConnectionBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public TestConnectionBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TestConnectionBuilder profileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
        return this;
    }

    public TestConnectionBuilder providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public TestConnectionBuilder providerUserId(String providerUserId) {
        this.providerUserId = providerUserId;
        return this;
    }

    public TestConnectionBuilder userProfile() {
        userProfileBuilder = new UserProfileBuilder();
        return this;
    }

    public TestConnection build() {
        ConnectionData connectionData = new ConnectionData(providerId,
                providerUserId,
                displayName,
                profileUrl,
                imageUrl,
                null,
                null,
                null,
                null);

        UserProfile userProfile = new UserProfileBuilder()
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();

        return new TestConnection(connectionData, userProfile);
    }
}
