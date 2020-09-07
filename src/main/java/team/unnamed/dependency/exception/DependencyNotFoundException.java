package team.unnamed.dependency.exception;

import team.unnamed.dependency.Dependency;

/**
 * An exception that is thrown if a
 * dependency isn't optional and
 * it's not found.
 */
public class DependencyNotFoundException extends RuntimeException {

    public DependencyNotFoundException(Dependency dependency) {
        super("Dependency not found: " + dependency.toString());
    }

    public DependencyNotFoundException(Throwable cause) {
        super(cause);
    }

    public DependencyNotFoundException(Dependency dependency, Throwable cause) {
        super("Dependency not found: " + dependency.toString(), cause);
    }

    public DependencyNotFoundException(Dependency dependency, String message) {
        super("Dependency not found: " + dependency.toString() + ". " + message);
    }

}
