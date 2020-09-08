package team.unnamed.dependency;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import team.unnamed.dependency.annotation.MavenLibrary;
import team.unnamed.dependency.annotation.MavenRepository;
import team.unnamed.dependency.resolve.DependencyResolver;
import team.unnamed.dependency.resolve.MavenDependencyResolver;

import java.util.List;

@MavenLibrary(
        groupId = "me.yushust.message",
        artifactId = "core",
        version = "1.0.0"
)
@MavenRepository("https://repo.unnamed.team/")
public class DependencyResolverTest {

    @Test
    public void test() {

        DependencyResolver<Class<?>> resolver = new MavenDependencyResolver();
        List<Dependency> dependencies = resolver.resolve(DependencyResolverTest.class);

        Dependency dependency = new MavenDependency(
                new String[]{"https://repo.unnamed.team/"},
                "me.yushust.message",
                "core",
                "1.0.0",
                false
        );

        Assertions.assertEquals(1, dependencies.size());
        Assertions.assertEquals(dependency, dependencies.get(0));

    }

}
