package team.unnamed.dependency;

import org.junit.jupiter.api.Test;
import team.unnamed.dependency.annotation.MavenLibrary;
import team.unnamed.dependency.annotation.MavenRepository;
import team.unnamed.dependency.exception.ErrorDetails;
import team.unnamed.dependency.version.MavenMetadataFinder;

import java.io.File;
import java.util.concurrent.Executors;

@MavenLibrary(
        groupId = "me.yushust.message",
        artifactId = "core",
        version = "1.0.0",
        repository = "https://repo.unnamed.team/repository/unnamed-releases"
)
@MavenLibrary(
        groupId = "org.junit.jupiter",
        artifactId = "junit-jupiter-api",
        version = "5.7.0-RC1",
        repository = MavenDependency.CENTRAL
)
@MavenRepository(MavenDependency.CENTRAL)
@MavenRepository("https://repo.unnamed.team/repository/unnamed-releases/")
public class DownloadTest {

    @Test
    public void test() {
        
        DependencyHandler handler = DependencyHandler.builder()
                .setDependenciesFolder(new File(System.getProperty("user.home") + "/Desktop/lib"))
                .setExecutor(Executors.newScheduledThreadPool(8))
                .setClassLoader(getClass().getClassLoader())
                .build();

        handler.setup(this);

    }

}
