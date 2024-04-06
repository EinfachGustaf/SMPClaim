package live.einfachgustaf.smpclaim

import live.einfachgustaf.smpclaim.commands.ClaimCommand
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.data.PostgresDataHandler
import live.einfachgustaf.smpclaim.listeners.Listeners
import live.einfachgustaf.smpclaim.utils.Config
import live.einfachgustaf.smpclaim.utils.WorldGuardApi
import live.einfachgustaf.smpclaim.utils.configs.DBConfig
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit

class SMPClaim : KSpigot() {

    companion object {
        lateinit var instance: SMPClaim; private set
        lateinit var worldGuardApi: WorldGuardApi; private set
        lateinit var dataHandler: IDataHandler; private set
        lateinit var dbConfig: Config; private set
        private var canEnable: Boolean = true
    }

    override fun load() {
        instance = this

        // ### Database ### //
        dbConfig = DBConfig()
        dbConfig.init()
        if (dbConfig.config.getString("type") == "postgres")
            dataHandler = PostgresDataHandler()
        else {
            logger.severe("Unsupported database type: ${dbConfig.config.getString("type")}. Disabling Plugin!")
            canEnable = false
        }
        try {
            dataHandler.init()
        } catch (e: Exception) {
            logger.severe("Error while initializing data handler: ${e.message}. Disabling Plugin!")
            canEnable = false
        }

        // ### WorldGuard ### //
        worldGuardApi = WorldGuardApi()
        worldGuardApi.init()
    }

    override fun startup() {
        if (!canEnable) {
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }
        Listeners().registerListeners()
        ClaimCommand().register()
    }

    override fun shutdown() {
    }

}