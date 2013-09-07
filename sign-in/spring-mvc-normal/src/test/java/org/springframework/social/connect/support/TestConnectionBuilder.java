package org.springframework.social.connect.support;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.UserProfile;

/**
 * @author Petri Kainulainen
 */
public class TestConnectionBuilder {

    private String displayName;

    private String imageUrl;

    private String profileUrl;

    private String providerId;

    private String providerUserId;

    private UserProfile userProfile;

    public TestConnectionBuilder() {

    }

    public TestConnectionBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public TestConnectionBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public TestConnectionBuilder userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
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
        return new TestConnection(connectionData, userProfile);
    }
}
