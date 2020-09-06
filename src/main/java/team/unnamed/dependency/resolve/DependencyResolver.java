package team.unnamed.dependency.resolve;

import team.unnamed.dependency.Dependency;

import java.util.List;

/**
 * Resolves dependencies of the provided
 * object.
 * @see AnnotationDependencyResolver
 * @see SubDependenciesResolver
 * @param <T> The type of object from which
 *           the dependencies will be resolved
 */
public interface DependencyResolver<T> {

    /**
     * Resolves the dependencies of
     * the specified object
     * @param object The object
     * @return The dependencies of
     * this object.
     */
    List<Dependency> resolve(T object);

}
