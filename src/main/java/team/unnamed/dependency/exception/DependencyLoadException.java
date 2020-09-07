package team.unnamed.dependency.exception;

import java.io.File;

/**
 * An exception that's thrown while
 * the Dependency Loader is being
 * created or when loading a Dependency
 */
public class DependencyLoadException extends RuntimeException {

    public DependencyLoadException(File file, Throwable cause) {
        super("Cannot load file: " + file.toString(), cause);
    }

    public DependencyLoadException(Throwable cause) {
        super(cause);
    }

    public DependencyLoadException(String message) {
        super(message);
    }

}
