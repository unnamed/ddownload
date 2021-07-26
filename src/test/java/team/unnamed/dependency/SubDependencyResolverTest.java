package team.unnamed.dependency;

import org.junit.jupiter.api.Test;
import team.unnamed.dependency.annotation.MavenLibrary;
import team.unnamed.dependency.logging.LogStrategy;
import team.unnamed.dependency.resolve.DependencyResolver;
import team.unnamed.dependency.resolve.MavenDependencyResolver;
import team.unnamed.dependency.resolve.SubDependenciesResolver;

import java.util.List;

@MavenLibrary(
        groupId = "com.github.twitch4j",
        artifactId ="twitch4j",
        filename = "t3.jar"
)
public class SubDependencyResolverTest {

    @Test
    public void test() {

        DependencyResolver<Class<?>> resolver = new MavenDependencyResolver(LogStrategy.getSilent());
        List<Dependency> dependencies = resolver.resolve(SubDependencyResolverTest.class);
        for (Dependency dependency : dependencies ) {
            List<Dependency> subDependencies = new SubDependenciesResolver().resolve(dependency);
            for (Dependency subDependency : subDependencies) {
                System.out.println(subDependency);
            }
        }
    }

}
