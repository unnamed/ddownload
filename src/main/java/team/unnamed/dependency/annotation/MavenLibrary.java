package team.unnamed.dependency.annotation;

import team.unnamed.dependency.exception.DependencyNotFoundException;

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
     * @return The maven dependency's
     * version.
     */
    String version() default "_LATEST_";

    /**
     * @return The maven dependency's
     * repository. Default isn't the
     * central because there are two
     * repository states:
     *
     * - SPECIFIED: The specified
     *      repository will be used,
     *      if the library isn't found,
     *      it uses the NON-SPECIFIED
     *      strategy
     *
     * - NON-SPECIFIED: Uses the repositories
     *      specified with {@link MavenRepository},
     *      if not found, uses the central.
     *      If not found in central, doesn't
     *      download it and checks if the
     *      dependency is optional, if it's
     *      not optional, results in a
     *      {@link DependencyNotFoundException}
     *      being thrown.
     */
    String repository() default "_NONE_";

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
