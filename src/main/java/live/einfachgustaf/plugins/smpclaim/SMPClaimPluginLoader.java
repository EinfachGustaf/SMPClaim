package live.einfachgustaf.plugins.smpclaim;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class SMPClaimPluginLoader implements PluginLoader {

    private final Set<Dependency> dependencies = Set.of(
            new Dependency(new DefaultArtifact("com.google.inject:guice:7.0.0"), null)
            );

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder("maven", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build());

        dependencies.forEach(resolver::addDependency);
        classpathBuilder.addLibrary(resolver);
    }
}
