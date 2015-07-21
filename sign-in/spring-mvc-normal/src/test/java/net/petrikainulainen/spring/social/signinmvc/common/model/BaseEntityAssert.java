package net.petrikainulainen.spring.social.signinmvc.common.model;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;

/**
 * @author Petri Kainulainen
 */
public class BaseEntityAssert extends AbstractAssert<BaseEntityAssert, BaseEntity> {

    public BaseEntityAssert(BaseEntity actual) {
        super(actual, BaseEntityAssert.class);
    }

    public static BaseEntityAssert assertThat(BaseEntity actual) {
        return new BaseEntityAssert(actual);
    }

    public BaseEntityAssert creationTimeAndModificationTimeAreEqual() {
        isNotNull();

        Assertions.assertThat(actual.getCreationTime())
                .overridingErrorMessage("Expected creation time to be equal than modification time but were <%s> and <%s>",
                        actual.getCreationTime(),
                        actual.getModificationTime()
                )
                .isEqualTo(actual.getModificationTime());

        return this;
    }

    public BaseEntityAssert creationTimeIsSet() {
        isNotNull();

        Assertions.assertThat(actual.getCreationTime())
                .overridingErrorMessage("Expected creation time to be set but was <null>")
                .isNotNull();

        return this;
    }

    public BaseEntityAssert modificationTimeIsAfterCreationTime() {
        isNotNull();

        DateTime creationTime = actual.getCreationTime();
        DateTime modificationTime = actual.getModificationTime();

        Assertions.assertThat(modificationTime.isAfter(creationTime))
                .overridingErrorMessage("Expected modification time to be after creation time but were <%s> and <%s>",
                        actual.getModificationTime(),
                        actual.getCreationTime()
                )
                .isTrue();

        return this;
    }

    public BaseEntityAssert modificationTimeIsSet() {
        isNotNull();

        Assertions.assertThat(actual.getModificationTime())
                .overridingErrorMessage("Expected modification time be set was <null>")
                .isNotNull();

        return this;
    }
}
