package live.einfachgustaf.smpclaim.listeners

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.event.listen
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.*
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent
import java.util.*

class Listeners {
    companion object {
        const val BYPASS_PERMISSION = "smpclaim.bypass"
    }

    private val msg = SMPClaim.listenerConfig.config.getString("message") ?: "§cDas kannst du hier nicht machen."
    private val canEntityInteraction = SMPClaim.listenerConfig.config.getBoolean("canEntityInteraction")
    private val canEntityDamage = SMPClaim.listenerConfig.config.getBoolean("canEntityDamage")
    private val canCreatureSpawn = SMPClaim.listenerConfig.config.getBoolean("canCreatureSpawn")
    private val canVehicleEntityCollision = SMPClaim.listenerConfig.config.getBoolean("canVehicleEntityCollision")
    private val canBlockBreak = SMPClaim.listenerConfig.config.getBoolean("canBlockBreak")
    private val canChangeBlock = SMPClaim.listenerConfig.config.getBoolean("canChangeBlock")
    private val canBlockPlace = SMPClaim.listenerConfig.config.getBoolean("canBlockPlace")
    private val canPlayerInteract = SMPClaim.listenerConfig.config.getBoolean("canPlayerInteract")
    private val canProjectileLaunch = SMPClaim.listenerConfig.config.getBoolean("canProjectileLaunch")
    private val canHangingBreakByEntity = SMPClaim.listenerConfig.config.getBoolean("canHangingBreakByEntity")
    private val canHangingPlace = SMPClaim.listenerConfig.config.getBoolean("canHangingPlace")
    private val canPlayerBucketFill = SMPClaim.listenerConfig.config.getBoolean("canPlayerBucketFill")
    private val canPlayerBucketEmpty = SMPClaim.listenerConfig.config.getBoolean("canPlayerBucketEmpty")
    private val canPlayerBucketEntity = SMPClaim.listenerConfig.config.getBoolean("canPlayerBucketEntity")
    private val canPlayerLeashEntity = SMPClaim.listenerConfig.config.getBoolean("canPlayerLeashEntity")
    private val canPlayerUnleashEntity = SMPClaim.listenerConfig.config.getBoolean("canPlayerUnleashEntity")
    private val canPlayerArmorStandManipulate = SMPClaim.listenerConfig.config.getBoolean("canPlayerArmorStandManipulate")
    private val canBlockExplode = SMPClaim.listenerConfig.config.getBoolean("canBlockExplode")
    private val canEntityExplode = SMPClaim.listenerConfig.config.getBoolean("canEntityExplode")
    private val canBlockSpread = SMPClaim.listenerConfig.config.getBoolean("canBlockSpread")
    private val canBlockPistonExtend = SMPClaim.listenerConfig.config.getBoolean("canBlockPistonExtend")
    private val canBlockPistonRetract = SMPClaim.listenerConfig.config.getBoolean("canBlockPistonRetract")
    private val canBlockFertilize = SMPClaim.listenerConfig.config.getBoolean("canBlockFertilize")


    fun registerListeners() {

        /**
         * Listener for when a player right-clicks on an entity.
         */
        listen<PlayerInteractEntityEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canEntityInteraction, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a entity is damaged by another entity/player
         */
        listen<EntityDamageByEntityEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canEntityDamage, it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a creature spawns
         */
        listen<CreatureSpawnEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canCreatureSpawn, it.entity, false)) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a vehicle collides with an entity
         */
        listen<VehicleEntityCollisionEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canVehicleEntityCollision, it.entity, false)) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a player breaks a block
         */
        listen<BlockBreakEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockBreak, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when an entity changes a block
         */
        listen<EntityChangeBlockEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canChangeBlock, it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a player places a block
         */
        listen<BlockPlaceEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockPlace, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player interacts with something
         */
        listen<PlayerInteractEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canPlayerInteract, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player launches a projectile
         */
        listen<ProjectileLaunchEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canProjectileLaunch, it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a player breaks a hanging entity
         */
        listen<HangingBreakByEntityEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canHangingBreakByEntity, it.remover, true)) return@listen
            sendMessage(it.remover)
            it.isCancelled = true
        }

        /**
         * Listener for when a hanging entity is placed
         */
        listen<HangingPlaceEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canHangingPlace, it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a player fills a bucket
         */
        listen<PlayerBucketFillEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canPlayerBucketFill, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player empties a bucket
         */
        listen<PlayerBucketEmptyEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canPlayerBucketEmpty, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player picks up an entity with a bucket
         */
        listen<PlayerBucketEntityEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canPlayerBucketEntity, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player leashes an entity
         */
        listen<PlayerLeashEntityEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canPlayerLeashEntity, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player unleashes an entity
         */
        listen<PlayerUnleashEntityEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canPlayerUnleashEntity, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player manipulates an armor stand
         */
        listen<PlayerArmorStandManipulateEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canPlayerArmorStandManipulate, it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a block explodes
         */
        listen<BlockExplodeEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockExplode, ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when an entity explodes
         */
        listen<EntityExplodeEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canEntityExplode, ChunkPosition(it.entity.location.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block spreads
         */
        listen<BlockSpreadEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockSpread, ChunkPosition(it.source.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is pushed by a piston
         */
        listen<BlockPistonExtendEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockPistonExtend, ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is pulled by a piston
         */
        listen<BlockPistonRetractEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockPistonRetract, ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is fertilized
         */
        listen<BlockFertilizeEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockFertilize, ChunkPosition(it.block.chunk), it.player)) return@listen
            it.isCancelled = true
        }

    }


    // --- HELPER METHODS --- //

    private fun isOwnerOrHasAccess(player: UUID, chunk: ChunkPosition): Boolean {
        return SMPClaim.dataHandler.isChunkClaimed(chunk) && SMPClaim.dataHandler.hasAccessOrIsOwner(player, chunk)
    }

    /**
     * Check if the event should be cancelled.
     * @param event The event to check.
     * @param check The boolean to check.
     * @param player The player who triggered the event.
     *
     * @return `true` if the event should be cancelled, `false` otherwise.
     */
    private fun check(event: Cancellable, check: Boolean, player: Player): Boolean {
        return check(event, check, ChunkPosition(player.location.chunk), player)
    }

    /**
     * Check if the event should be cancelled.
     * @param event The event to check.
     * @param check The boolean to check.
     * @param chunk The chunk the event happens in.
     * @param triggerer The player who triggered the event, if the event exposes one.
     *
     * @return `true` if the event should be cancelled, `false` otherwise.
     */
    private fun check(event: Cancellable, check: Boolean, chunk: ChunkPosition, triggerer: Player? = null): Boolean {
        val bypasses = triggerer?.hasPermission(BYPASS_PERMISSION) ?: false
        val ownerOrMember = triggerer?.let { isOwnerOrHasAccess(it.uniqueId, chunk) } ?: false
        return shouldCancelClaimEvent(
            chunkClaimed = SMPClaim.dataHandler.isChunkClaimed(chunk),
            flagAllows = check,
            alreadyCancelled = event.isCancelled,
            bypasses = bypasses,
            ownerOrMember = ownerOrMember
        )
    }

    /**
     * Check if the event should be cancelled.
     * @param event The event to check.
     * @param check The boolean to check.
     * @param entity The entity who triggered the event.
     * @param entityMaybePlayer Whether the entity should be treated as a player when it is one.
     *
     * @return `true` if the event should be cancelled, `false` otherwise.
     */
    private fun check(event: Cancellable, check: Boolean, entity: Entity, entityMaybePlayer: Boolean): Boolean {
        val triggerer = if (entityMaybePlayer && entity is Player) entity else null
        return check(event, check, ChunkPosition(entity.location.chunk), triggerer)
    }

    private fun sendMessage(player: Player) {
        player.sendMessage(msg)
    }
    private fun sendMessage(entity: Entity) {
        if (entity is Player) entity.sendMessage(msg)
    }

}

/**
 * The actual claim decision, extracted from the listeners so it can be unit tested without a server.
 *
 * @return `true` if the listener should cancel the event, `false` if it should let it pass.
 */
internal fun shouldCancelClaimEvent(
    chunkClaimed: Boolean,
    flagAllows: Boolean,
    alreadyCancelled: Boolean,
    bypasses: Boolean,
    ownerOrMember: Boolean
): Boolean {
    return !(!chunkClaimed || alreadyCancelled || flagAllows || bypasses || ownerOrMember)
}