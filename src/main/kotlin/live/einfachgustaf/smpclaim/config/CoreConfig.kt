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

    @Comment("Explosion-specific claim settings. own-claim: whether explosions triggered by a player who owns or has access to the claim are cancelled. filter-foreign-claims: whether blocks in foreign claims are removed from explosion lists instead of cancelling the event entirely.")
    val explosionSettings = ExplosionSettings()

    @Comment("Version of the config file. Do not change this value manually.")
    @Required
    val configVersion: String = "1.3"
}

/**
 * Claim-related explosion behavior settings.
 */
@ConfigSerializable
class ExplosionSettings {

    @Comment("When true, explosions with a recognised player triggerer who owns or is a member of the claim will not be cancelled.")
    val ownClaim = true

    @Comment("When true, blocks located in chunks belonging to other players are removed from explosion block lists instead of cancelling the event entirely. When false, existing cancellation behaviour applies.")
    val filterForeignClaims = true
}