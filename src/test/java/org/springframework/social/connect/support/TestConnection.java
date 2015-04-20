package org.springframework.social.connect.support;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.UserProfile;

/**
 * @author Petri Kainulainen
 */
public class TestConnection extends AbstractConnection {

    private ConnectionData connectionData;

    private UserProfile userProfile;

    public TestConnection(ConnectionData connectionData, UserProfile userProfile) {
        super(connectionData, null);
        this.connectionData = connectionData;
        this.userProfile = userProfile;
    }

    @Override
    public UserProfile fetchUserProfile() {
        return userProfile;
    }

    @Override
    public Object getApi() {
        return null;
    }

    @Override
    public ConnectionData createData() {
        return connectionData;
    }
}
