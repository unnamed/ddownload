package team.unnamed.dependency.resolve;

import team.unnamed.dependency.Dependency;
import team.unnamed.dependency.MavenDependency;
import team.unnamed.dependency.annotation.MavenLibrary;
import team.unnamed.dependency.annotation.MavenRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolves the dependencies of a class using
 * its type annotations.
 */
public class AnnotationDependencyResolver implements DependencyResolver<Class<?>> {

    @Override
    public List<Dependency> resolve(Class<?> clazz) {

        List<String> repositories = new ArrayList<>();
        List<Dependency> dependencies = new ArrayList<>();

        for (MavenRepository repository : clazz.getAnnotationsByType(MavenRepository.class)) {
            repositories.add(repository.value());
        }

        for (MavenLibrary library : clazz.getAnnotationsByType(MavenLibrary.class)) {
            List<String> libraryRepositories = new ArrayList<>();
            String repository = library.repository();
            if (!repository.isEmpty() && !repository.equals("_NONE_")) {
                libraryRepositories.add(repository);
            }
            libraryRepositories.addAll(repositories);
            Dependency dependency = new MavenDependency(
                    libraryRepositories.toArray(new String[0]),
                    library.groupId(),
                    library.artifactId(),
                    library.version()
            );
            dependencies.add(dependency);
        }

        return dependencies;
    }

}
