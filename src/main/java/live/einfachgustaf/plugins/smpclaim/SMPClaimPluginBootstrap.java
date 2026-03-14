package live.einfachgustaf.plugins.smpclaim;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import live.einfachgustaf.plugins.smpclaim.modules.SMPClaimModule;
import org.bukkit.plugin.java.JavaPlugin;

public class SMPClaimPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {

    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        SMPClaim plugin = new SMPClaim();
        SMPClaimModule module = new SMPClaimModule(plugin);
        Injector injector = Guice.createInjector(module);

        plugin.setInjector(injector);
        injector.injectMembers(plugin);
        return plugin;
    }
}
