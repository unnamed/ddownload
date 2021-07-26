package team.unnamed.dependency;

/**
 * Represents an application dependency.
 * This dependency can be formatted to
 * an array of URLs (where the dependency
 * can be downloaded from)
 */
public interface Dependency {

    /**
     * @return True if the dependency
     * is optional.
     */
    boolean isOptional();

    /**
     * @return The urls where the
     * dependency can be downloaded
     * from.
     */
    String[] getPossibleUrls();

    /**
     * @return The urls where the
     * dependency are origin
     */
    String[] getPossibleOriginUrls();

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
     */
    String toString();

}
