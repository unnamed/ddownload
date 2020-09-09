package team.unnamed.dependency.annotation;

import java.lang.annotation.*;

/**
 * Specifies that the annotated class
 * depends on this maven library.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MavenLibrary.MavenLibraries.class)
@Documented
public @interface MavenLibrary {

    /**
     * @return The maven dependency's
     * group id.
     */
    String groupId();

    /**
     * @return The maven dependency's
     * artifact id.
     */
    String artifactId();

    /**
     * The already downloaded filename.
     * Default is artifactId-version.jar
     * @return The filename
     */
    String filename() default "_DEFAULT_";

    /**
     * @return The maven dependency's
     * version.
     */
    String version() default "_LATEST_";

    /**
     * @return True if the dependency is optional
     */
    boolean optional() default false;

    /**
     * @return The maven dependency's
     * repository.
     */
    String repoId() default "central";

    /**
     * Specifies many maven libraries.
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface MavenLibraries {

        MavenLibrary[] value();

    }

}
