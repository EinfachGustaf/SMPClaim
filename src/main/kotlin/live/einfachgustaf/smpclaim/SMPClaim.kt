package live.einfachgustaf.smpclaim

import live.einfachgustaf.smpclaim.commands.AccessCommand
import live.einfachgustaf.smpclaim.commands.ChunkInfoCommand
import live.einfachgustaf.smpclaim.commands.ClaimCommand
import live.einfachgustaf.smpclaim.commands.UnclaimCommand
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.data.PostgresDataHandler
import live.einfachgustaf.smpclaim.data.local.LocalDataHandler
import live.einfachgustaf.smpclaim.listeners.Listeners
import live.einfachgustaf.smpclaim.types.DataHandlerType
import live.einfachgustaf.smpclaim.utils.Config
import live.einfachgustaf.smpclaim.utils.WorldGuardApi
import live.einfachgustaf.smpclaim.utils.configs.DBConfig
import live.einfachgustaf.smpclaim.utils.configs.ListenerConfig
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit

class SMPClaim : KSpigot() {

    companion object {
        lateinit var instance: SMPClaim; private set
        lateinit var worldGuardApi: WorldGuardApi; private set
        lateinit var dataHandler: IDataHandler; private set
        lateinit var dbConfig: Config; private set
        lateinit var listenerConfig: Config; private set
        private var canEnable: Boolean = true
    }

    override fun load() {
        instance = this

        // ### Database ### //
        dbConfig = DBConfig()
        dbConfig.init()
        try {
            val configType = dbConfig.config.getString("type")

            if (configType == null) {
                logger.severe("Config entry 'type' not found. Disabling Plugin!")
                canEnable = false
                return
            }

            dataHandler = DataHandlerType.valueOf(
                configType.uppercase()
            ).dataHandler

        } catch (e: IllegalArgumentException) {
            logger.severe("Data handler not found: ${e.message}. Disabling Plugin!")
            canEnable = false
        }

        try {
            dataHandler.init()
        } catch (e: Exception) {
            e.printStackTrace()
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

        // ### Listeners ### //
        listenerConfig = ListenerConfig()
        listenerConfig.init()
        Listeners().registerListeners()

        // ### Commands ### //
        AccessCommand.register()
        ClaimCommand.register()
        UnclaimCommand.register()
        ChunkInfoCommand.register()
    }

    override fun shutdown() {
        try {
            dataHandler.exit()
        } catch (e: UninitializedPropertyAccessException) {
            logger.severe("'dataHandler' is not initialized")
        }
    }
}