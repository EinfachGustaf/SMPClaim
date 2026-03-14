package live.einfachgustaf.plugins.smpclaim.modules;

import com.google.inject.AbstractModule;
import live.einfachgustaf.plugins.smpclaim.SMPClaim;
import org.bukkit.plugin.java.JavaPlugin;

public class SMPClaimModule extends AbstractModule {

    private final SMPClaim plugin;

    public SMPClaimModule(SMPClaim plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(SMPClaim.class).toInstance(plugin);
    }
}

