package team.unnamed.dependency;

import team.unnamed.dependency.exception.DependencyDownloadException;
import team.unnamed.dependency.exception.DependencyLoadException;

import java.io.File;
import java.util.Arrays;

/**
 * Main class of the entire
 * library. Facade design pattern
 */
public interface DependencyHandler {

    /**
     * @return Where the dependencies
     * will be saved.
     */
    File getDestinyFolder();

    /**
     * Downloads and adds the dependency to
     * the classpath.
     * @throws DependencyLoadException If an
     * error occurred while adding a dependency
     * to the classpath.
     * @throws DependencyDownloadException If an
     * error occurred while downloading the
     * dependency file.
     * @param dependencies The dependencies
     */
    void setup(Iterable<? extends Dependency> dependencies);

    /**
     * Downloads and adds the dependency to
     * the classpath.
     * @throws DependencyLoadException If an
     * error occurred while adding a dependency
     * to the classpath.
     * @throws DependencyDownloadException If
     * a dependency isn't optional and it isn't
     * found.
     * @param dependencies The dependencies
     */
    default void setup(Dependency... dependencies) {
        setup(Arrays.asList(dependencies));
    }

    /**
     * Downloads the specified dependencies.
     * @throws DependencyDownloadException If
     * a dependency isn't optional and it isn't
     * found.
     * @param dependencies The dependencies.
     * @return The downloaded files
     */
    File[] download(Iterable<? extends Dependency> dependencies);

    /**
     * Downloads the specified dependencies.
     * @throws DependencyDownloadException If
     * a dependency isn't optional and it isn't
     * found.
     * @param dependencies The dependencies.
     * @return The downloaded files
     */
    default File[] download(Dependency... dependencies) {
        return download(Arrays.asList(dependencies));
    }

}
