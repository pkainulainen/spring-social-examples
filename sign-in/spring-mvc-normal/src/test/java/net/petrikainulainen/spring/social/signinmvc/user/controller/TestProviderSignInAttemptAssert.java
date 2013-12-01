package net.petrikainulainen.spring.social.signinmvc.user.controller;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.springframework.social.connect.web.TestProviderSignInAttempt;

/**
 * @author Petri Kainulainen
 */
public class TestProviderSignInAttemptAssert extends GenericAssert<TestProviderSignInAttemptAssert, TestProviderSignInAttempt> {

    private TestProviderSignInAttemptAssert(TestProviderSignInAttempt actual) {
        super(TestProviderSignInAttemptAssert.class, actual);
    }

    public static TestProviderSignInAttemptAssert assertThatSignIn(TestProviderSignInAttempt actual) {
        return new TestProviderSignInAttemptAssert(actual);
    }

    public TestProviderSignInAttemptAssert createdNoConnections() {
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

    public TestProviderSignInAttemptAssert createdConnectionForUserId(String userId) {
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
