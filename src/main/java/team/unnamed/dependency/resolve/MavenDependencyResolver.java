package team.unnamed.dependency.resolve;

import team.unnamed.dependency.Dependency;
import team.unnamed.dependency.MavenDependency;
import team.unnamed.dependency.annotation.MavenLibrary;
import team.unnamed.dependency.annotation.MavenRepository;
import team.unnamed.dependency.exception.ErrorDetails;
import team.unnamed.dependency.logging.LogStrategy;
import team.unnamed.dependency.util.Validate;
import team.unnamed.dependency.version.MavenMetadataFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves the dependencies of a class using
 * its type annotations.
 */
public class MavenDependencyResolver implements DependencyResolver<Class<?>> {

    private final LogStrategy logger;

    public MavenDependencyResolver(LogStrategy logger) {
        this.logger = Validate.notNull(logger, "logger");
    }

    @Override
    public List<Dependency> resolve(Class<?> clazz) {

        Map<String, String> repositoriesById = new HashMap<>();
        List<Dependency> dependencies = new ArrayList<>();

        repositoriesById.put("central", MavenDependency.CENTRAL);
        for (MavenRepository repository : clazz.getAnnotationsByType(MavenRepository.class)) {
            repositoriesById.put(repository.id(), repository.url());
        }

        ErrorDetails errorDetails = new ErrorDetails("Cannot resolve dependencies");

        for (MavenLibrary library : clazz.getAnnotationsByType(MavenLibrary.class)) {

            String repository = repositoriesById.get(library.repoId());

            if (repository == null || repository.isEmpty()) {
                logger.error("Unknown repository id: " + library.repoId() + " for dependency "
                        + library.groupId() + ":" + library.artifactId());
                continue;
            }

            String version = library.version();

            if (version.equals("_LATEST_")) {
                version = MavenMetadataFinder.getLatestVersion(
                        repository, library.groupId(), library.artifactId(), errorDetails
                );
            }

            Dependency dependency = new MavenDependency(
                    new String[] {repository},
                    library.groupId(),
                    library.artifactId(),
                    version,
                    library.filename(),
                    library.optional()
            );
            dependencies.add(dependency);
        }

        if (errorDetails.errorCount() > 0) {
            throw errorDetails.toResolveException();
        }

        return dependencies;
    }

}
