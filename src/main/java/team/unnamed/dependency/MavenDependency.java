package team.unnamed.dependency;

import team.unnamed.dependency.util.Urls;

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

    private final String repository;
    private final String groupId;
    private final String artifactId;
    private final String version;

    public MavenDependency(String repository, String groupId, String artifactId,
                           String version, String artifactName) {
        this.artifactName = artifactName;
        this.repository = repository;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public MavenDependency(String repository, String groupId, String artifactId, String version) {
        this(repository, groupId, artifactId, version, artifactId + "-" + version + ".jar");
    }

    public MavenDependency(String groupId, String artifactId, String version) {
        this(CENTRAL, groupId, artifactId, version);
    }

    @Override
    public String toUrl() {
        return Urls.endWithSlash(repository) + Urls.dotsToSlashes(groupId)
                + "/" + artifactId + "/" + version + "/" + artifactId
                + "-" + version + ".jar";
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
        return Objects.equals(artifactName, that.artifactName) &&
                Objects.equals(repository, that.repository) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactName, repository, groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return "Maven Dependency " + groupId + ":" + artifactId
                + ":" + version + " in " + repository;
    }

}
