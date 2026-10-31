package live.einfachgustaf.smpclaim

import live.einfachgustaf.smpclaim.commands.AccessCommand
import live.einfachgustaf.smpclaim.commands.ChunkInfoCommand
import live.einfachgustaf.smpclaim.commands.ClaimCommand
import live.einfachgustaf.smpclaim.commands.UnclaimCommand
import live.einfachgustaf.smpclaim.config.ConfigManager
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.listeners.Listeners
import live.einfachgustaf.smpclaim.utils.Config
import live.einfachgustaf.smpclaim.utils.WorldGuardApi
import live.einfachgustaf.smpclaim.utils.configs.ListenerConfig
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

class SMPClaim : KSpigot() {

    companion object {
        lateinit var instance: SMPClaim; private set
        lateinit var worldGuardApi: WorldGuardApi; private set
        lateinit var dataHandler: IDataHandler; private set
        lateinit var listenerConfig: Config; private set
    }

    private var canEnable: Boolean = true
    val configManager = ConfigManager()

    override fun load() {
        instance = this
        configManager.initialize()

        dataHandler = configManager.config.dataHandlerType.dataHandler

        try {
            dataHandler.init()
        } catch (e: Exception) {
            e.printStackTrace()
            logger.severe("Error while initializing data handler: ${e.message}. Disabling Plugin!")
            canEnable = false
        }

        worldGuardApi = WorldGuardApi()
        if (!worldGuardApi.init()) {
            logger.warning("Failed to register the chunk-claim WorldGuard flag; WorldGuard region flags will not affect claiming.")
        }
    }

    override fun startup() {
        if (!canEnable) {
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        // Everyone may claim unless a permissions plugin says otherwise; undeclared permissions
        // would default to operators only.
        val pluginManager = Bukkit.getPluginManager()
        if (pluginManager.getPermission(ClaimCommand.PERMISSION) == null) {
            pluginManager.addPermission(Permission(ClaimCommand.PERMISSION, "Allows claiming chunks", PermissionDefault.TRUE))
        }

        listenerConfig = ListenerConfig()
        listenerConfig.init()
        Listeners().registerListeners()

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