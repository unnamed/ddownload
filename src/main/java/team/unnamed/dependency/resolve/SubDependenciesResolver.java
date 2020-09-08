package team.unnamed.dependency.resolve;

import team.unnamed.dependency.Dependency;
import team.unnamed.dependency.MavenDependency;

import java.util.List;

/**
 * Resolves the dependencies of
 * another dependency.
 */
public class SubDependenciesResolver implements DependencyResolver<Dependency> {

    @Override
    public List<Dependency> resolve(Dependency dependency) {
        return null;
    }

}
