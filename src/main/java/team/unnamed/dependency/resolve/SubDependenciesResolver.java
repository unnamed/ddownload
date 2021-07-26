package team.unnamed.dependency.resolve;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import team.unnamed.dependency.Dependency;
import team.unnamed.dependency.MavenDependency;
import team.unnamed.dependency.exception.ErrorDetails;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
public final class SubDependenciesResolver implements DependencyResolver<Dependency> {

    protected static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    @Override
    public List<Dependency> resolve(Dependency dependency) {

        Set<String> repositories = new HashSet<>();

        repositories.add(MavenDependency.CENTRAL);
        ErrorDetails errorDetails = new ErrorDetails("Cannot resolve dependencies");
        List<Dependency> dependencies = new ArrayList<>();
        String pomUrl = dependency.getPossibleOriginUrls()[0];
        try {
            URL url = new URL(pomUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "ddownloader");
            try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                DocumentBuilder docBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
                Document document = docBuilder.parse(inputStream);
                document.getDocumentElement().normalize();
                NodeList repositoryList = document.getElementsByTagName("repository");
                for (int i = 0; i < repositoryList.getLength(); i++) {
                    Node node = repositoryList.item(i);
                    if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                    Element element = (Element) node;
                    repositories.add(element.getElementsByTagName("url").item(0).getTextContent());
                }
                NodeList dependencyList = document.getElementsByTagName("dependency");
                for (int i = 0; i < dependencyList.getLength(); i++) {
                    Node node = dependencyList.item(i);
                    if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                    Element element = (Element) node;
                    String groupId = element.getElementsByTagName("groupId").item(0).getTextContent();
                    String artifactId = element.getElementsByTagName("artifactId").item(0).getTextContent();
                    String version = element.getElementsByTagName("version").item(0).getTextContent();
                    String scope = "compile";
                    if (element.getElementsByTagName("scope").getLength() != 0) {
                        scope = element.getElementsByTagName("scope").item(0).getTextContent();
                    }
                    if (!scope.equalsIgnoreCase("compile")) continue;
                    Dependency subDependency = new MavenDependency(
                            repositories.toArray(new String[]{}),
                            groupId,
                            artifactId,
                            version,
                            artifactId.toLowerCase() + ".jar",
                            false
                    );
                    dependencies.add(subDependency);
                }
            } catch (IOException e) {
                errorDetails.add(e);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            errorDetails.add(e);
        }

        if (errorDetails.errorCount() > 0) {
            throw errorDetails.toResolveException();
        }
        return dependencies;
    }
}
