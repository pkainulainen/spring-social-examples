package net.petrikainulainen.spring.social.signinmvc.common.model;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.joda.time.DateTime;

/**
 * @author Petri Kainulainen
 */
public class BaseEntityAssert extends GenericAssert<BaseEntityAssert, BaseEntity> {

    public BaseEntityAssert(BaseEntity actual) {
        super(BaseEntityAssert.class, actual);
    }

    public static BaseEntityAssert assertThat(BaseEntity actual) {
        return new BaseEntityAssert(actual);
    }

    public BaseEntityAssert creationTimeAndModificationTimeAreEqual() {
        isNotNull();

        String errorMessage = String.format(
                "Expected creation time to be equal than modification time but were <%s> and <%s>",
                actual.getCreationTime(),
                actual.getModificationTime()
        );
        Assertions.assertThat(actual.getCreationTime())
                .overridingErrorMessage(errorMessage)
                .isEqualTo(actual.getModificationTime());

        return this;
    }

    public BaseEntityAssert creationTimeIsSet() {
        isNotNull();

        String errorMessage = "Expected creation time to be set but was <null>";
        Assertions.assertThat(actual.getCreationTime()).overridingErrorMessage(errorMessage).isNotNull();

        return this;
    }

    public BaseEntityAssert modificationTimeIsAfterCreationTime() {
        isNotNull();

        String errorMessage = String.format(
                "Expected modification time to be after creation time but were <%s> and <%s>",
                actual.getModificationTime(),
                actual.getCreationTime()
        );

        DateTime creationTime = actual.getCreationTime();
        DateTime modificationTime = actual.getModificationTime();

        Assertions.assertThat(modificationTime.isAfter(creationTime))
                .overridingErrorMessage(errorMessage)
                .isTrue();

        return this;
    }

    public BaseEntityAssert modificationTimeIsSet() {
        isNotNull();

        String errorMessage = "Expected modification time be set was <null>";
        Assertions.assertThat(actual.getModificationTime())
                .overridingErrorMessage(errorMessage)
                .isNotNull();

        return this;
    }
}
