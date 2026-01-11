package live.einfachgustaf.smpclaim.config

import live.einfachgustaf.smpclaim.SMPClaim
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.serialize.SerializationException
import kotlin.io.path.Path
import kotlin.io.path.exists

class ConfigManager(private val plugin: SMPClaim) {

    lateinit var config: CoreConfig

    fun initialize() {
        val configFile = Path(plugin.dataFolder.path, "core.conf")
        val loader = HoconConfigurationLoader.builder()
            .path(configFile)
            .build()

        val node = if (configFile.exists())
            loader.load()
         else
            loader.createNode()

        config = try {
            node.get(CoreConfig::class.java) ?: CoreConfig()
        } catch (ex: SerializationException) {
            plugin.slF4JLogger.warn("Error loading config, default values \u200B\u200Bwill be used\n", ex)
            CoreConfig()
        }

        try {
            node.set(CoreConfig::class.java, config)
            loader.save(node)
        } catch (ex: Exception) {
            plugin.slF4JLogger.error("Error saving config\n", ex)
        }
    }
}
