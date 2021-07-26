package team.unnamed.dependency;

import team.unnamed.dependency.util.Urls;
import team.unnamed.dependency.util.Validate;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a dependency constructed using
 * a repository, groupId, artifactId and version.
 * These properties are formatted to create a
 * URL like this:
 *  "https://repository/groupId/artifactId/version/artifactId-version.jar"
 */
public class MavenDependency implements Dependency {

    public static final String CENTRAL = "https://repo1.maven.org/maven2/";

    private final String artifactName;
    private final boolean optional;

    private final String[] repositories;
    private final String groupId;
    private final String artifactId;
    private final String version;

    public MavenDependency(String[] repositories, String groupId, String artifactId,
                           String version, String artifactName, boolean optional) {
        this.artifactName = Validate.notEmpty(artifactName);
        this.optional = optional;
        this.repositories = Validate.eachNotEmpty(repositories);
        this.groupId = Validate.notEmpty(groupId);
        this.artifactId = Validate.notEmpty(artifactId);
        this.version = Validate.notEmpty(version);
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    @Override
    public String[] getPossibleUrls() {
        String[] possibleUrls = new String[repositories.length];
        for (int i = 0; i < possibleUrls.length; i++) {
            String repository = repositories[i];
            String url = Urls.endWithSlash(repository) + Urls.dotsToSlashes(groupId)
                    + "/" + artifactId + "/" + version + "/" + artifactId
                    + "-" + version + ".jar";
            possibleUrls[i] = url;
        }
        return possibleUrls;
    }

    @Override
    public String[] getPossibleOriginUrls() {
        String[] possibleUrls = new String[repositories.length];
        for (int i = 0; i < possibleUrls.length; i++) {
            String repository = repositories[i];
            String url = Urls.endWithSlash(repository) + Urls.dotsToSlashes(groupId)
                    + "/" + artifactId + "/" + version + "/" + artifactId
                    + "-" + version + ".pom";
            possibleUrls[i] = url;
        }
        return possibleUrls;
    }

    @Override
    public String getArtifactName() {
        return artifactName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MavenDependency)) {
            return false;
        }
        MavenDependency that = (MavenDependency) o;
        return artifactName.equals(that.artifactName) &&
                Arrays.equals(repositories, that.repositories) &&
                groupId.equals(that.groupId) &&
                artifactId.equals(that.artifactId) &&
                version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactName, repositories, groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return "Maven Dependency " + groupId + ":" + artifactId
                + ":" + version + " in " + String.join(" or ", repositories);
    }

}
