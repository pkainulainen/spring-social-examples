package net.petrikainulainen.spring.social.signinmvc.common.model;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static net.petrikainulainen.spring.social.signinmvc.common.model.BaseEntityAssert.assertThat;

/**
 * @author Petri Kainulainen
 */
public class BaseEntityTest {

    @Test
    public void prePersist_ShouldSetCreationAndModificationTime() {
        TestEntity entity = new TestEntity();

        entity.prePersist();

        assertThat(entity)
                .creationTimeIsSet()
                .modificationTimeIsSet()
                .creationTimeAndModificationTimeAreEqual();
    }

    @Test
    public void preUpdated_ShouldUpdateModificationTime() {
        DateTime yesterday = DateTime.now().minusDays(1);

        TestEntity entity = new TestEntityBuilder()
                .creationTime(yesterday)
                .modificationTime(yesterday)
                .build();

        entity.preUpdate();

        assertThat(entity)
                .creationTimeIsSet()
                .modificationTimeIsSet()
                .modificationTimeIsAfterCreationTime();
    }

    private static class TestEntity extends BaseEntity<Long> {
        private Long id;

        @Override
        public Long getId() {
            return id;
        }
    }

    private static class TestEntityBuilder {
        private TestEntity build;

        private TestEntityBuilder() {
            build = new TestEntity();
        }

        private TestEntityBuilder creationTime(DateTime creationTime) {
            ReflectionTestUtils.setField(build, "creationTime", creationTime);
            return this;
        }

        private TestEntityBuilder modificationTime(DateTime modificationTime) {
            ReflectionTestUtils.setField(build, "modificationTime", modificationTime);
            return this;
        }

        private TestEntity build() {
            return build;
        }
    }
}
