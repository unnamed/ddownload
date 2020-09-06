package team.unnamed.dependency;

/**
 * Represents an application dependency.
 * This dependency can be formatted to
 * a URL (where the dependency will be
 * downloaded from)
 */
public interface Dependency {

    /**
     * @return The url where the
     * dependency will be downloaded
     * from.
     */
    String toUrl();

    /**
     * @return The already downloaded
     * file name. The dependency handler
     * checks if a file with this name
     * exists in the dependencies folder.
     * If not present, then, tries to
     * download it.
     */
    String getArtifactName();

    /**
     * @return Dependency information
     * @implNote Include details
     */
    String toString();

}
