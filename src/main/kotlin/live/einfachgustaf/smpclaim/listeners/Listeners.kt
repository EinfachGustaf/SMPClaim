package live.einfachgustaf.smpclaim.listeners

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.event.listen
import org.bukkit.GameMode
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
            handlePlayerEvent(it, canEntityInteraction, it.player)
        }

        /**
         * Listener for when a entity is damaged by another entity/player
         */
        listen<EntityDamageByEntityEvent> {
            handleEntityEvent(it, canEntityDamage, it.entity, true)
        }

        /**
         * Listener for when a creature spawns
         */
        listen<CreatureSpawnEvent> {
            handleEntityEvent(it, canCreatureSpawn, it.entity, false)
        }

        /**
         * Listener for when a vehicle collides with an entity
         */
        listen<VehicleEntityCollisionEvent> {
            handleEntityEvent(it, canVehicleEntityCollision, it.entity, false)
        }

        /**
         * Listener for when a player breaks a block
         */
        listen<BlockBreakEvent> {
            handlePlayerEvent(it, canBlockBreak, it.player)
        }

        /**
         * Listener for when an entity changes a block
         */
        listen<EntityChangeBlockEvent> {
            handleEntityEvent(it, canChangeBlock, it.entity, true)
        }

        /**
         * Listener for when a player places a block
         */
        listen<BlockPlaceEvent> {
            handlePlayerEvent(it, canBlockPlace, it.player)
        }

        /**
         * Listener for when a player interacts with something
         */
        listen<PlayerInteractEvent> {
            handlePlayerEvent(it, canPlayerInteract, it.player)
        }

        /**
         * Listener for when a player launches a projectile
         */
        listen<ProjectileLaunchEvent> {
            handleEntityEvent(it, canProjectileLaunch, it.entity, true)
        }

        /**
         * Listener for when a player breaks a hanging entity
         */
        listen<HangingBreakByEntityEvent> {
            handleEntityEvent(it, canHangingBreakByEntity, it.remover, true)
        }

        /**
         * Listener for when a hanging entity is placed
         */
        listen<HangingPlaceEvent> {
            handleEntityEvent(it, canHangingPlace, it.entity, true)
        }

        /**
         * Listener for when a player fills a bucket
         */
        listen<PlayerBucketFillEvent> {
            handlePlayerEvent(it, canPlayerBucketFill, it.player)
        }

        /**
         * Listener for when a player empties a bucket
         */
        listen<PlayerBucketEmptyEvent> {
            handlePlayerEvent(it, canPlayerBucketEmpty, it.player)
        }

        /**
         * Listener for when a player picks up an entity with a bucket
         */
        listen<PlayerBucketEntityEvent> {
            handlePlayerEvent(it, canPlayerBucketEntity, it.player)
        }

        /**
         * Listener for when a player leashes an entity
         */
        listen<PlayerLeashEntityEvent> {
            handlePlayerEvent(it, canPlayerLeashEntity, it.player)
        }

        /**
         * Listener for when a player unleashes an entity
         */
        listen<PlayerUnleashEntityEvent> {
            handlePlayerEvent(it, canPlayerUnleashEntity, it.player)
        }

        /**
         * Listener for when a player manipulates an armor stand
         */
        listen<PlayerArmorStandManipulateEvent> {
            handlePlayerEvent(it, canPlayerArmorStandManipulate, it.player)
        }

        /**
         * Listener for when a block explodes
         */
        listen<BlockExplodeEvent> {
            handleChunkEvent(it, canBlockExplode, ChunkPosition(it.block.chunk))
        }

        /**
         * Listener for when an entity explodes
         */
        listen<EntityExplodeEvent> {
            handleChunkEvent(it, canEntityExplode, ChunkPosition(it.entity.location.chunk))
        }

        /**
         * Listener for when a block spreads
         */
        listen<BlockSpreadEvent> {
            handleChunkEvent(it, canBlockSpread, ChunkPosition(it.source.chunk))
        }

        /**
         * Listener for when a block is pushed by a piston
         */
        listen<BlockPistonExtendEvent> {
            handleChunkEvent(it, canBlockPistonExtend, ChunkPosition(it.block.chunk))
        }

        /**
         * Listener for when a block is pulled by a piston
         */
        listen<BlockPistonRetractEvent> {
            handleChunkEvent(it, canBlockPistonRetract, ChunkPosition(it.block.chunk))
        }

        /**
         * Listener for when a block is fertilized
         */
        listen<BlockFertilizeEvent> {
            handleChunkEvent(it, canBlockFertilize, ChunkPosition(it.block.chunk))
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
        // Return false if the player is admin
        if (player.gameMode == GameMode.CREATIVE) return false

        val chunk = player.location.chunk

        // Return false if the chunk is not claimed
        if (!SMPClaim.dataHandler.isChunkClaimed(ChunkPosition(chunk))) return false

        // Return false if the event is cancelled ||or it is allowed ||or the player is the owner of the chunk or has access to the chunk
        if (event.isCancelled || check || isOwnerOrHasAccess(player.uniqueId, ChunkPosition(chunk))) return false

        // else return true
        return true
    }

    /**
     * Check if the event should be cancelled.
     * @param event The event to check.
     * @param check The boolean to check.
     *
     * @return `true` if the event should be cancelled, `false` otherwise.
     */
    private fun check(event: Cancellable, check: Boolean, chunk: ChunkPosition): Boolean {

        // Return false if the chunk is not claimed
        if (!SMPClaim.dataHandler.isChunkClaimed(chunk)) return false

        // Return false if the event is cancelled ||or it is allowed ||or the player is the owner of the chunk or has access to the chunk
        if (event.isCancelled || check) return false

        // else return true
        return true
    }

    /**
     * Check if the event should be cancelled.
     * @param event The event to check.
     * @param check The boolean to check.
     * @param entity The entity who triggered the event.
     *
     * @return `true` if the event should be cancelled, `false` otherwise.
     */
    private fun check(event: Cancellable, check: Boolean, entity: Entity, entityMaybePlayer: Boolean): Boolean {

        // Return false if the event should not be cancelled
        if (check) return false

        val chunk = entity.location.chunk

        // Return false if the chunk is not claimed
        if (!SMPClaim.dataHandler.isChunkClaimed(ChunkPosition(chunk))) return false

        // Only if the entity is a player and should be treated as a player
        if (entity is Player && entityMaybePlayer) {
            val player: Player = entity

            // Return false if the player is admin
            if (player.gameMode == GameMode.CREATIVE) return false

            // Return false if the event is cancelled ||or the player is the owner of the chunk or has access to the chunk
            if (event.isCancelled || isOwnerOrHasAccess(player.uniqueId, ChunkPosition(chunk))) return false
        }

        // else return true
        return true
    }

    private fun sendMessage(entity: Entity) {
        if (entity is Player) entity.sendMessage(msg)
    }
    
    /**
     * Helper to handle player-based events consistently.
     * Checks if event should be cancelled and handles cancellation and messaging.
     */
    private inline fun <reified T : Cancellable> handlePlayerEvent(
        event: T,
        canPerform: Boolean,
        player: Player
    ) {
        if (!check(event, canPerform, player)) return
        sendMessage(player)
        event.isCancelled = true
    }
    
    /**
     * Helper to handle entity-based events consistently.
     * Checks if event should be cancelled and handles cancellation and messaging.
     */
    private inline fun <reified T : Cancellable> handleEntityEvent(
        event: T,
        canPerform: Boolean,
        entity: Entity,
        entityMaybePlayer: Boolean
    ) {
        if (!check(event, canPerform, entity, entityMaybePlayer)) return
        sendMessage(entity)
        event.isCancelled = true
    }
    
    /**
     * Helper to handle chunk-based events consistently.
     * Checks if event should be cancelled based on chunk position.
     */
    private inline fun <reified T : Cancellable> handleChunkEvent(
        event: T,
        canPerform: Boolean,
        chunk: ChunkPosition
    ) {
        if (!check(event, canPerform, chunk)) return
        event.isCancelled = true
    }

}