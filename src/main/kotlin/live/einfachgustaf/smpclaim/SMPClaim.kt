package live.einfachgustaf.smpclaim

import com.google.inject.Guice
import com.google.inject.Injector
import live.einfachgustaf.smpclaim.commands.AccessCommand
import live.einfachgustaf.smpclaim.commands.ChunkInfoCommand
import live.einfachgustaf.smpclaim.commands.ClaimCommand
import live.einfachgustaf.smpclaim.commands.UnclaimCommand
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.di.SMPClaimModule
import live.einfachgustaf.smpclaim.listeners.Listeners
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit

class SMPClaim : KSpigot() {

    private lateinit var injector: Injector
    private var canEnable: Boolean = true

    override fun load() {
        try {
            // Initialize Guice injector
            injector = Guice.createInjector(SMPClaimModule(this))
        } catch (e: Exception) {
            e.printStackTrace()
            logger.severe("Error while initializing dependency injection: ${e.message}. Disabling Plugin!")
            canEnable = false
        }
    }

    override fun startup() {
        if (!canEnable) {
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        // Register listeners
        injector.getInstance(Listeners::class.java).registerListeners()

        // Register commands
        injector.getInstance(AccessCommand::class.java).register()
        injector.getInstance(ClaimCommand::class.java).register()
        injector.getInstance(UnclaimCommand::class.java).register()
        injector.getInstance(ChunkInfoCommand::class.java).register()
    }

    override fun shutdown() {
        try {
            injector.getInstance(IDataHandler::class.java).exit()
        } catch (e: Exception) {
            logger.severe("Error shutting down data handler: ${e.message}")
        }
    }
}