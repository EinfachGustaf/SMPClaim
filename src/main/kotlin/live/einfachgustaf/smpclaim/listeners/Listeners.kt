package live.einfachgustaf.smpclaim.listeners

import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.utils.Config
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
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Listeners @Inject constructor(
    private val dataHandler: IDataHandler,
    @Named("listenerConfig") private val listenerConfig: Config
) {
    
    private val msg = listenerConfig.config.getString("message") ?: "§cDas kannst du hier nicht machen."
    private val canEntityInteraction = listenerConfig.config.getBoolean("canEntityInteraction")
    private val canEntityDamage = listenerConfig.config.getBoolean("canEntityDamage")
    private val canCreatureSpawn = listenerConfig.config.getBoolean("canCreatureSpawn")
    private val canVehicleEntityCollision = listenerConfig.config.getBoolean("canVehicleEntityCollision")
    private val canBlockBreak = listenerConfig.config.getBoolean("canBlockBreak")
    private val canChangeBlock = listenerConfig.config.getBoolean("canChangeBlock")
    private val canBlockPlace = listenerConfig.config.getBoolean("canBlockPlace")
    private val canPlayerInteract = listenerConfig.config.getBoolean("canPlayerInteract")
    private val canProjectileLaunch = listenerConfig.config.getBoolean("canProjectileLaunch")
    private val canHangingBreakByEntity = listenerConfig.config.getBoolean("canHangingBreakByEntity")
    private val canHangingPlace = listenerConfig.config.getBoolean("canHangingPlace")
    private val canPlayerBucketFill = listenerConfig.config.getBoolean("canPlayerBucketFill")
    private val canPlayerBucketEmpty = listenerConfig.config.getBoolean("canPlayerBucketEmpty")
    private val canPlayerBucketEntity = listenerConfig.config.getBoolean("canPlayerBucketEntity")
    private val canPlayerLeashEntity = listenerConfig.config.getBoolean("canPlayerLeashEntity")
    private val canPlayerUnleashEntity = listenerConfig.config.getBoolean("canPlayerUnleashEntity")
    private val canPlayerArmorStandManipulate = listenerConfig.config.getBoolean("canPlayerArmorStandManipulate")
    private val canBlockExplode = listenerConfig.config.getBoolean("canBlockExplode")
    private val canEntityExplode = listenerConfig.config.getBoolean("canEntityExplode")
    private val canBlockSpread = listenerConfig.config.getBoolean("canBlockSpread")
    private val canBlockPistonExtend = listenerConfig.config.getBoolean("canBlockPistonExtend")
    private val canBlockPistonRetract = listenerConfig.config.getBoolean("canBlockPistonRetract")
    private val canBlockFertilize = listenerConfig.config.getBoolean("canBlockFertilize")


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
            if (!check(it, canBlockFertilize, ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

    }


    // --- HELPER METHODS --- //

    private fun isOwnerOrHasAccess(player: UUID, chunk: ChunkPosition): Boolean {
        return dataHandler.isChunkClaimed(chunk) && dataHandler.hasAccessOrIsOwner(player, chunk)
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
        if (!dataHandler.isChunkClaimed(ChunkPosition(chunk))) return false

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
        if (!dataHandler.isChunkClaimed(chunk)) return false

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
        if (!dataHandler.isChunkClaimed(ChunkPosition(chunk))) return false

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