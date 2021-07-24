package team.unnamed.dependency.version;

import team.unnamed.dependency.exception.ErrorDetails;
import team.unnamed.dependency.util.Urls;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Collection of methods for
 * Maven libraries handling.
 */
public final class MavenMetadataFinder {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();

    /**
     * Finds the latest version of a specified dependency using
     * its repository, groupId and artifactId.
     *
     * @param repository   The repository
     * @param groupId      The dependency groupId
     * @param artifactId   The dependency artifactId
     * @param errorDetails The error details
     * @return The latest version, null if not found.
     */
    public static String getLatestVersion(String repository, String groupId, String artifactId,
                                          ErrorDetails errorDetails) {

        String metadataUrl = Urls.endWithSlash(repository) + Urls.dotsToSlashes(groupId)
                + "/" + artifactId + "/maven-metadata.xml";

        String releaseVersion = null;
        try {
            URL url = new URL(metadataUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "ddownloader");


            try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                XMLStreamReader reader = XML_INPUT_FACTORY.createXMLStreamReader(inputStream);
                while (reader.hasNext()) {
                    if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                        if (reader.getLocalName().equals("release")) {
                            releaseVersion = reader.getElementText();
                        }
                        if (reader.getLocalName().equals("latest")) {
                            return reader.getElementText();
                        }
                    }
                }
            } catch (XMLStreamException e) {
                errorDetails.add(e);
            }
        } catch (IOException e) {
            errorDetails.add(e);
        }
        return releaseVersion;
    }

}
