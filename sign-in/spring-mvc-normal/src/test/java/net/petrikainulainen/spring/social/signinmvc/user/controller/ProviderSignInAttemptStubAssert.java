package net.petrikainulainen.spring.social.signinmvc.user.controller;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.springframework.social.connect.web.ProviderSignInAttemptStub;

/**
 * @author Petri Kainulainen
 */
public class ProviderSignInAttemptStubAssert extends GenericAssert<ProviderSignInAttemptStubAssert, ProviderSignInAttemptStub> {

    public ProviderSignInAttemptStubAssert(ProviderSignInAttemptStub actual) {
        super(ProviderSignInAttemptStubAssert.class, actual);
    }

    public static ProviderSignInAttemptStubAssert assertThatSignIn(ProviderSignInAttemptStub actual) {
        return new ProviderSignInAttemptStubAssert(actual);
    }

    public ProviderSignInAttemptStubAssert createdNoConnections() {
        isNotNull();

        String error = String.format(
                "Expected that no connections were created but found <%d> connection",
                actual.getConnections().size()
        );
        Assertions.assertThat(actual.getConnections())
                .overridingErrorMessage(error)
                .isEmpty();

        return this;
    }

    public ProviderSignInAttemptStubAssert createdConnectionForUserId(String userId) {
        isNotNull();

        String error = String.format(
                "Expected that connection was created for user id <%s> but found none.",
                userId
        );

        Assertions.assertThat(actual.getConnections())
                .overridingErrorMessage(error)
                .contains(userId);

        return this;
    }
}
