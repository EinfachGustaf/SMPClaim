package live.einfachgustaf.smpclaim.config

import live.einfachgustaf.smpclaim.types.DataHandlerType
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
class CoreConfig {

    @Comment("Type of data handler to use. Options: LOCAL_JSON, POSTGRESQL")
    @Required
    val dataHandlerType = DataHandlerType.LOCAL_JSON

    val postgresConfig = PostgresConfig()

    @Comment("Version of the config file. Do not change this value manually.")
    @Required
    val configVersion: String = "1.2"
}