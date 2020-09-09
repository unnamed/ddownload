package team.unnamed.dependency.annotation;

import java.lang.annotation.*;

/**
 * Represents a maven repository
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MavenRepository.MavenRepositories.class)
@Documented
public @interface MavenRepository {

    /**
     * @return The repository id, used in
     * {@link MavenLibrary#repoId}
     */
    String id();

    /**
     * @return The url of the repository.
     */
    String url();

    /**
     * Holds many maven repositories
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface MavenRepositories {

        MavenRepository[] value();

    }

}
