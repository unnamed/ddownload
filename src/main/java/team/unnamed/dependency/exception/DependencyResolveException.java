package team.unnamed.dependency.exception;

import team.unnamed.dependency.resolve.DependencyResolver;

/**
 * An exception that can be thrown
 * while resolving dependencies
 * with {@link DependencyResolver}
 */
public class DependencyResolveException extends RuntimeException {

    public DependencyResolveException(String message) {
        super(message);
    }

}
