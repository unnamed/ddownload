package team.unnamed.dependency.resolve;

import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import team.unnamed.dependency.Dependency;
import team.unnamed.dependency.MavenDependency;
import team.unnamed.dependency.exception.ErrorDetails;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resolves the dependencies of
 * another dependency.
 */
public class SubDependenciesResolver implements DependencyResolver<Dependency> {

    @Override
    public List<Dependency> resolve(Dependency dependency) {

        Set<String> repositories = new HashSet<>();

        repositories.add(MavenDependency.CENTRAL);
        ErrorDetails errorDetails = new ErrorDetails("Cannot resolve dependencies");
        Set<Dependency> dependencies = new HashSet<>();
        String pomUrl = dependency.getPossibleOriginUrls()[0];
        try {
            URL url = new URL(pomUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "ddownloader");
            try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                Model model = new MavenXpp3Reader().read(inputStream);
                for (Repository repository : model.getRepositories()) {
                    repositories.add(repository.getUrl());
                }
                for (org.apache.maven.model.Dependency mavenDependency : model.getDependencies()) {
                    if (!mavenDependency.getScope().equalsIgnoreCase("compile")) continue;
                    String version = mavenDependency.getVersion();
                    Dependency subDependency = new MavenDependency(
                            repositories.toArray(new String[]{}),
                            mavenDependency.getGroupId(),
                            mavenDependency.getArtifactId(),
                            version,
                            mavenDependency.getArtifactId().toLowerCase() + ".jar",
                            false
                    );
                    dependencies.add(subDependency);

                }
            } catch (XmlPullParserException e) {
                errorDetails.add(e);
            }
        } catch (IOException e) {
            errorDetails.add(e);
        }

        if (errorDetails.errorCount() > 0) {
            throw errorDetails.toResolveException();
        }

        return new ArrayList<>(dependencies);
    }
}
