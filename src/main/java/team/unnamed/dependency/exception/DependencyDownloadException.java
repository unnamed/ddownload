package team.unnamed.dependency.exception;

/**
 * An exception that can be thrown while
 * loading a dependency.
 */
public class DependencyDownloadException extends RuntimeException {

    public DependencyDownloadException(String message) {
        super(message);
    }

}
