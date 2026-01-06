package live.einfachgustaf.smpclaim;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.Set;

/**
 * Custom PluginLoader to load dependencies via Maven
 */
@SuppressWarnings("UnstableApiUsage")
public class SMPClaimPluginLoader implements PluginLoader {

    private final Set<Dependency> dependencies = Set.of(
            new Dependency(new DefaultArtifact("net.axay:kspigot:1.21.0"), null),
            new Dependency(new DefaultArtifact("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"), null)
    );

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder("maven", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build());

        dependencies.forEach(resolver::addDependency);
        classpathBuilder.addLibrary(resolver);
    }
}
