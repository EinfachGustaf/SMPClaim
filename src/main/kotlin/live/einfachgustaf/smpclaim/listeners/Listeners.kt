package live.einfachgustaf.smpclaim.listeners

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.PlayerArmorStandManipulateEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketEntityEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerUnleashEntityEvent
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent
import java.util.*

class Listeners {

    // --- STATIC SHIT THAT SHOULD BE A CONFIG --- //
    // TODO: Move this to a config
    private val msg = "Das kannst du hier nicht machen."
    private val canEntityInteraction = false
    private val canEntityDamage = false
    private val canCreatureSpawn = false
    private val canVehicleEntityCollision = false
    private val canBlockBreak = false
    private val canChangeBlock = false
    private val canBlockPlace = false
    private val canPlayerInteract = false
    private val canProjectileLaunch = false
    private val canHangingBreakByEntity = false
    private val canHangingPlace = false
    private val canPlayerBucketFill = false
    private val canPlayerBucketEmpty = false
    private val canPlayerBucketEntity = false
    private val canPlayerLeashEntity = false
    private val canPlayerUnleashEntity = false
    private val canPlayerArmorStandManipulate = false
    private val canBlockExplode = false
    private val canEntityExplode = false
    private val canBlockSpread = false
    private val canBlockPistonExtendEvent = false
    private val canBlockPistonRetractEvent = false
    private val canBlockFertilize = false


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
            if (!check(it, canBlockPistonExtendEvent, ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is pulled by a piston
         */
        listen<BlockPistonRetractEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockPistonRetractEvent, ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is fertilized
         */
        listen<BlockFertilizeEvent> {
            // Return if the event should not be cancelled
            if (!check(it, canBlockFertilize, ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

    }


    // --- HELPER METHODS --- //

    private fun isOwnerOrHasAccess(player: UUID, chunk: ChunkPosition): Boolean {
        return SMPClaim.dataHandler.isChunkClaimed(chunk) && SMPClaim.dataHandler.getChunkOwner(chunk) == player
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

    private fun sendMessage(player: Player) {
        player.sendMessage(msg)
    }
    private fun sendMessage(entity: Entity) {
        if (entity is Player) entity.sendMessage(msg)
    }

}