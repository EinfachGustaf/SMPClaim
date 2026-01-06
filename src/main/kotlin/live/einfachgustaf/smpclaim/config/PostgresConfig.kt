package live.einfachgustaf.smpclaim.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
class PostgresConfig {

    val postgresUrl = "jdbc:postgresql://localhost:5432/smpclaim"

    val postgresUser = "smpclaim"

    val postgresPassword = "someverysecurepassword"
}