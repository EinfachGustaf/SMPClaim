package live.einfachgustaf.plugins.smpclaim;

import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public class SMPClaim extends JavaPlugin {

    private Injector injector;

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    public <T> T getInstance(Class<T> type) {
        if (injector == null) {
            throw new IllegalStateException("Injector not initialized");
        }
        return injector.getInstance(type);
    }
}
