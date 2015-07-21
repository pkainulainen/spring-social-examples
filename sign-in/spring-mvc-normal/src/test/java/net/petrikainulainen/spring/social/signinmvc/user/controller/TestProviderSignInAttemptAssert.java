package net.petrikainulainen.spring.social.signinmvc.user.controller;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.springframework.social.connect.web.TestProviderSignInAttempt;

/**
 * @author Petri Kainulainen
 */
public class TestProviderSignInAttemptAssert extends AbstractAssert<TestProviderSignInAttemptAssert, TestProviderSignInAttempt> {

    private TestProviderSignInAttemptAssert(TestProviderSignInAttempt actual) {
        super(actual, TestProviderSignInAttemptAssert.class);
    }

    public static TestProviderSignInAttemptAssert assertThatSignIn(TestProviderSignInAttempt actual) {
        return new TestProviderSignInAttemptAssert(actual);
    }

    public TestProviderSignInAttemptAssert createdNoConnections() {
        isNotNull();

        Assertions.assertThat(actual.getConnections())
                .overridingErrorMessage( "Expected that no connections were created but found <%d> connection",
                        actual.getConnections().size()
                )
                .isEmpty();

        return this;
    }

    public TestProviderSignInAttemptAssert createdConnectionForUserId(String userId) {
        isNotNull();

        Assertions.assertThat(actual.getConnections())
                .overridingErrorMessage("Expected that connection was created for user id <%s> but found none.",
                        userId
                )
                .contains(userId);

        return this;
    }
}
