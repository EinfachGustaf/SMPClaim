package live.einfachgustaf.smpclaim.listeners

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.event.listen
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
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

    // --- Pre-computed config flags (cached at startup) ---

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

    // --- Configurable protection switches ---

    private val creativeBypass = SMPClaim.listenerConfig.config.getBoolean("bypass.creative", false)
    private val permissionBypassEnabled = SMPClaim.listenerConfig.config.getBoolean("bypass.permission", true)
    private val ownClaimExplosion = SMPClaim.listenerConfig.config.getBoolean("explosions.own-claim", true)
    private val filterForeignClaims = SMPClaim.listenerConfig.config.getBoolean("explosions.filter-foreign-claims", true)

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
         * Listener for when a block explodes.
         * No triggerer source — relies on chunk-level check + owner-or-member.
         */
        listen<BlockExplodeEvent> {
            if (!check(it, canBlockExplode, ChunkPosition(it.block.chunk), triggerer = null, isExplosionEvent = true)) return@listen
            if (filterForeignClaims) {
                applyForeignFilterToBlocks(it.blockList(), ChunkPosition(it.block.chunk), null)
            } else {
                it.isCancelled = true
            }
        }

        /**
         * Listener for when an entity explodes.
         * Extracts TNTPrimed source as optional triggerer; applies full ownership /
         * bypass logic plus block filtering for foreign claims.
         */
        listen<EntityExplodeEvent> {
            val triggerer = (it.entity as? TNTPrimed)?.source as? Player
            if (!check(it, canEntityExplode, ChunkPosition(it.entity.location.chunk), triggerer, isExplosionEvent = true)) return@listen
            // Bypass-active triggerers skip filtering entirely: their explosions
            // keep full vanilla block destruction.
            val bypassActive = (creativeBypass && triggerer != null && triggerer.gameMode == GameMode.CREATIVE) ||
                (permissionBypassEnabled && triggerer?.hasPermission(BYPASS_PERMISSION) == true)
            if (filterForeignClaims && !bypassActive) {
                applyForeignFilterToBlocks(it.blockList(), ChunkPosition(it.entity.location.chunk), triggerer)
            }
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
    private fun check(
        event: Cancellable,
        check: Boolean,
        chunk: ChunkPosition,
        triggerer: Player? = null
    ): Boolean {
        return check(event, check, chunk, triggerer, isExplosionEvent = false)
    }

    /**
     * Full check used by both regular listeners and explosion handlers.
     */
    private fun check(
        event: Cancellable,
        check: Boolean,
        chunk: ChunkPosition,
        triggerer: Player?,
        isExplosionEvent: Boolean
    ): Boolean {
        val rawPermission = triggerer?.hasPermission(BYPASS_PERMISSION) ?: false
        val effectiveBypasses = permissionBypassEnabled && rawPermission
        val ownerOrMember = triggerer?.let { isOwnerOrHasAccess(it.uniqueId, chunk) } ?: false
        val creativeActive = creativeBypass && triggerer != null && triggerer.gameMode == GameMode.CREATIVE
        return shouldCancelClaimEvent(
            chunkClaimed = SMPClaim.dataHandler.isChunkClaimed(chunk),
            flagAllows = check,
            alreadyCancelled = event.isCancelled,
            bypasses = rawPermission,
            ownerOrMember = ownerOrMember,
            creativeBypassActive = creativeActive,
            permissionBypassEnabled = this.permissionBypassEnabled,
            ownerExplosionBypassEnabled = this.ownClaimExplosion,
            isExplosionEvent = isExplosionEvent
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

    /**
     * Filter [blocks] in-place: remove any block whose chunk falls inside a
     * foreign claim (claimed by someone else whom the triggerer does not own/access).
     *
     * No triggerer means unknown cause — conservatively remove all claimed-chunk blocks.
     */
    private fun applyForeignFilterToBlocks(
        blocks: MutableCollection<org.bukkit.block.Block>,
        originChunk: ChunkPosition,
        triggerer: Player?
    ) {
        val triggererUuid = triggerer?.uniqueId
        val keptChunks = filterBlocksInForeignClaims(
            originChunkPos = originChunk,
            triggererId = triggererUuid,
            blocks = blocks.map { ChunkPosition(it.location.chunk) },
            isChunkClaimed = { SMPClaim.dataHandler.isChunkClaimed(it) },
            hasAccessOrIsOwner = { uuid, pos -> isOwnerOrHasAccess(uuid, pos) }
        ).toSet()
        // Capture the surviving blocks BEFORE clearing the live list.
        val keptBlocks = blocks.filter { ChunkPosition(it.location.chunk) in keptChunks }
        blocks.clear()
        blocks.addAll(keptBlocks)
    }

    private fun sendMessage(player: Player) {
        player.sendMessage(msg)
    }
    private fun sendMessage(entity: Entity) {
        if (entity is Player) entity.sendMessage(msg)
    }

}

/**
 * Claim-event cancellation decision extracted into a pure function.
 *
 * @param chunkClaimed          does the relevant chunk have an active claim?
 * @param flagAllows            does the config-flag permit this action type?
 * @param alreadyCancelled      has some other handler already cancelled the event?
 * @param bypasses              raw hasPermission result passed by the caller.
 * @param ownerOrMember         is the triggerer owner or member of the claim?
 * @param creativeBypassActive  when true, creative-mode players are exempt from claims
 * @param permissionBypassEnabled when true and [bypasses] is true, the event passes
 * @param ownerExplosionBypassEnabled when true and [isExplosionEvent] is true,
 *                                    owner/members triggering explosions bypass cancellation
 * @param isExplosionEvent      whether this is an explosion-type event
 *
 * @return `true` if the listener should cancel the event, `false` otherwise.
 */
internal fun shouldCancelClaimEvent(
    chunkClaimed: Boolean,
    flagAllows: Boolean,
    alreadyCancelled: Boolean,
    bypasses: Boolean,
    ownerOrMember: Boolean,
    creativeBypassActive: Boolean = false,
    permissionBypassEnabled: Boolean = true,
    ownerExplosionBypassEnabled: Boolean = true,
    isExplosionEvent: Boolean = false
): Boolean {
    // Creative-mode bypass: active when BOTH the config switch is on AND
    // the triggering player is in creative mode. Combined in one param to keep
    // the pure-function interface clean.
    if (creativeBypassActive) {
        return false
    }

    // Permission-bypass switch gate: even if the player has the permission,
    // the config can disable it entirely.
    val effectiveBypass = permissionBypassEnabled && bypasses

    // Owner-or-member gate: for explosion events it only applies when
    // own-claim-explosions are enabled, so the config switch is observable.
    // Non-explosion events keep the normal owner-or-member gate.
    val ownerAllowed = ownerOrMember && (!isExplosionEvent || ownerExplosionBypassEnabled)

    return !(!chunkClaimed || alreadyCancelled || flagAllows || effectiveBypass || ownerAllowed)
}

/**
 * Pure block-filtering function for explosion events (Part 2).
 * Returns the subset of blocks that should remain after removing those in
 * foreign claims. Does NOT depend on any Bukkit objects — only positions
 * and functional lookups make it unit-testable.
 *
 * @param originChunkPos chunk where the explosion event originates
 * @param triggererId    UUID of the player who triggered the event, or null (BlockExplodeEvent has no triggerer)
 * @param blocks         list of block positions affected by the explosion
 * @param isChunkClaimed lookup: does this chunk belong to any claim?
 * @param hasAccessOrIsOwner lookup: does this player own or have access to this chunk?
 * @return the filtered subset of [blocks] that should survive the explosion
 *
 * Logic (Lead decision):
 * - Wildnis (unclaimed chunks): blocks always survive.
 * - Owned claim: blocks survive if triggerer owns/is member of the claim.
 * - Foreign claim: blocks are removed (the core "filter-foreign-claims" behavior).
 * - No triggerer: ALL blocks in claimed chunks are removed — without knowing WHO
 *   caused the explosion we protect claimed areas by assuming they might be foreign.
 *   This prevents unowned explosions from destroying another player's property.
 */
internal fun filterBlocksInForeignClaims(
    originChunkPos: ChunkPosition,
    triggererId: java.util.UUID?,
    blocks: List<ChunkPosition>,
    isChunkClaimed: (ChunkPosition) -> Boolean,
    hasAccessOrIsOwner: (java.util.UUID, ChunkPosition) -> Boolean
): List<ChunkPosition> {
    return blocks.filter { blockPos ->
        val claimed = isChunkClaimed(blockPos)
        if (!claimed) {
            true // Wildnis: keep all blocks
        } else {
            // Claimed — keep only if triggerer owns or has access.
            // Without a triggerer we cannot verify ownership → remove (conservative safety).
            triggererId != null && hasAccessOrIsOwner(triggererId, blockPos)
        }
    }
}
