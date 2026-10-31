package live.einfachgustaf.smpclaim.listeners

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.event.listen
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

    /**
     * All listener behaviour knobs in one record so that the pure decision
     * functions can be unit-tested without mocking anything.
     */
    data class Settings(
        val creativeBypass: Boolean,
        val permissionBypass: Boolean,
        val ownClaimExplosion: Boolean,
        val filterForeignClaims: Boolean
    )

    fun registerListeners() {

        /**
         * Listener for when a player right-clicks on an entity.
         */
        listen<PlayerInteractEntityEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canEntityInteraction"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a entity is damaged by another entity/player
         */
        listen<EntityDamageByEntityEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canEntityDamage"), it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a creature spawns
         */
        listen<CreatureSpawnEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canCreatureSpawn"), it.entity, false)) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a vehicle collides with an entity
         */
        listen<VehicleEntityCollisionEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canVehicleEntityCollision"), it.entity, false)) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a player breaks a block
         */
        listen<BlockBreakEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canBlockBreak"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when an entity changes a block
         */
        listen<EntityChangeBlockEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canChangeBlock"), it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a player places a block
         */
        listen<BlockPlaceEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canBlockPlace"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player interacts with something
         */
        listen<PlayerInteractEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canPlayerInteract"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player launches a projectile
         */
        listen<ProjectileLaunchEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canProjectileLaunch"), it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a player breaks a hanging entity
         */
        listen<HangingBreakByEntityEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canHangingBreakByEntity"), it.remover, true)) return@listen
            sendMessage(it.remover)
            it.isCancelled = true
        }

        /**
         * Listener for when a hanging entity is placed
         */
        listen<HangingPlaceEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canHangingPlace"), it.entity, true)) return@listen
            sendMessage(it.entity)
            it.isCancelled = true
        }

        /**
         * Listener for when a player fills a bucket
         */
        listen<PlayerBucketFillEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canPlayerBucketFill"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player empties a bucket
         */
        listen<PlayerBucketEmptyEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canPlayerBucketEmpty"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player picks up an entity with a bucket
         */
        listen<PlayerBucketEntityEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canPlayerBucketEntity"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player leashes an entity
         */
        listen<PlayerLeashEntityEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canPlayerLeashEntity"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player unleashes an entity
         */
        listen<PlayerUnleashEntityEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canPlayerUnleashEntity"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a player manipulates an armor stand
         */
        listen<PlayerArmorStandManipulateEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canPlayerArmorStandManipulate"), it.player)) return@listen
            sendMessage(it.player)
            it.isCancelled = true
        }

        /**
         * Listener for when a block explodes.
         * No triggerer source — relies on chunk-level check + owner-or-member.
         */
        listen<BlockExplodeEvent> {
            handleBlockExplode(it)
        }

        /**
         * Listener for when an entity explodes.
         * Extracts TNTPrimed source as optional triggerer; applies full ownership /
         * bypass logic plus block filtering for foreign claims.
         */
        listen<EntityExplodeEvent> {
            handleEntityExplode(it)
        }

        /**
         * Listener for when a block spreads
         */
        listen<BlockSpreadEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canBlockSpread"), ChunkPosition(it.source.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is pushed by a piston
         */
        listen<BlockPistonExtendEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canBlockPistonExtend"), ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is pulled by a piston
         */
        listen<BlockPistonRetractEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canBlockPistonRetract"), ChunkPosition(it.block.chunk))) return@listen
            it.isCancelled = true
        }

        /**
         * Listener for when a block is fertilized
         */
        listen<BlockFertilizeEvent> {
            if (!check(it, SMPClaim.listenerConfig.config.getBoolean("canBlockFertilize"), ChunkPosition(it.block.chunk), it.player)) return@listen
            it.isCancelled = true
        }

    }


    // --- EXPLOSION HANDLERS --- //

    /**
     * BlockExplodeEvent handler: respects claim ownership via shouldCancelClaimEvent
     * (owner/members allowed through), then optionally filters out blocks in
     * foreign-claim chunks instead of cancelling the event entirely.
     */
    private fun handleBlockExplode(event: BlockExplodeEvent) {
        val flagAllows = SMPClaim.listenerConfig.config.getBoolean("canBlockExplode")
        val pos = ChunkPosition(event.block.location.chunk)
        val triggerer: Player? = null
        val alreadyCancelled = event.isCancelled

        val st = settings()
        // Creative bypass
        if (st.creativeBypass && triggerer != null && triggerer.gameMode == org.bukkit.GameMode.CREATIVE) {
            return
        }

        val bypasses = !st.permissionBypass || (triggerer?.hasPermission(BYPASS_PERMISSION) ?: false)
        val ownerOrMember = triggerer?.let { isOwnerOrHasAccess(it.uniqueId, pos) } ?: false

        val cancelled = shouldCancelClaimEvent(
            chunkClaimed = SMPClaim.dataHandler.isChunkClaimed(pos),
            flagAllows = flagAllows,
            alreadyCancelled = alreadyCancelled,
            bypasses = bypasses,
            ownerOrMember = ownerOrMember,
            creativeBypassActive = st.creativeBypass && triggerer != null && triggerer.gameMode == org.bukkit.GameMode.CREATIVE,
            permissionBypassEnabled = st.permissionBypass,
            ownerExplosionBypassEnabled = st.ownClaimExplosion,
            filterForeignClaims = st.filterForeignClaims,
            triggererTriggered = triggerer != null
        )

        if (cancelled) {
            event.isCancelled = true
        } else if (st.filterForeignClaims) {
            applyForeignFilterToBlocks(event.blockList(), pos, triggerer)
        }
    }

    /**
     * EntityExplodeEvent handler: extracts triggerer from TNTPrimed.source,
     * applies full ownership / bypass logic including creative and permission checks.
     * Supports block-level filtering for foreign claims.
     */
    private fun handleEntityExplode(event: EntityExplodeEvent) {
        val triggerer = (event.entity as? TNTPrimed)?.source as? Player
        val flagAllows = SMPClaim.listenerConfig.config.getBoolean("canEntityExplode")
        val pos = ChunkPosition(event.entity.location.chunk)
        val alreadyCancelled = event.isCancelled

        val st = settings()
        // Creative-bypass: allow creative players to ignore claim protection
        if (st.creativeBypass && triggerer != null && triggerer.gameMode == org.bukkit.GameMode.CREATIVE) {
            return
        }

        val bypasses = !st.permissionBypass || (triggerer?.hasPermission(BYPASS_PERMISSION) ?: false)
        val ownerOrMember = triggerer?.let { isOwnerOrHasAccess(it.uniqueId, pos) } ?: false

        val cancelled = shouldCancelClaimEvent(
            chunkClaimed = SMPClaim.dataHandler.isChunkClaimed(pos),
            flagAllows = flagAllows,
            alreadyCancelled = alreadyCancelled,
            bypasses = bypasses,
            ownerOrMember = ownerOrMember,
            creativeBypassActive = st.creativeBypass && triggerer != null && triggerer.gameMode == org.bukkit.GameMode.CREATIVE,
            permissionBypassEnabled = st.permissionBypass,
            ownerExplosionBypassEnabled = st.ownClaimExplosion,
            filterForeignClaims = st.filterForeignClaims,
            triggererTriggered = triggerer != null
        )

        if (cancelled) {
            event.isCancelled = true
        } else if (st.filterForeignClaims) {
            applyForeignFilterToBlocks(event.blockList(), pos, triggerer)
        }
    }

    /**
     * Filter [blocks] in-place: remove any block whose chunk falls inside a
     * foreign claim (claimed by someone else whom the triggerer does not own/access).
     */
    private fun applyForeignFilterToBlocks(
        blocks: MutableCollection<org.bukkit.block.Block>,
        originChunk: ChunkPosition,
        triggerer: Player?
    ) {
        val filtered = blocks.filter { block ->
            val bcPos = ChunkPosition(block.location.chunk)
            val isClaimed = SMPClaim.dataHandler.isChunkClaimed(bcPos)
            if (!isClaimed) {
                true  // Wildnis: keep
            } else {
                // Claimed — keep only if triggerer owns or has access
                triggerer == null || isOwnerOrHasAccess(triggerer.uniqueId, bcPos)
            }
        }
        blocks.clear()
        blocks.addAll(filtered)
    }

    // --- HELPER METHODS --- //

    private fun isOwnerOrHasAccess(player: UUID, chunk: ChunkPosition): Boolean {
        return SMPClaim.dataHandler.isChunkClaimed(chunk) && SMPClaim.dataHandler.hasAccessOrIsOwner(player, chunk)
    }

    private fun settings(): Settings {
        return Settings(
            creativeBypass = SMPClaim.listenerConfig.config.getBoolean("bypass.creative", false),
            permissionBypass = SMPClaim.listenerConfig.config.getBoolean("bypass.permission", true),
            ownClaimExplosion = SMPClaim.listenerConfig.config.getBoolean("explosions.own-claim", true),
            filterForeignClaims = SMPClaim.listenerConfig.config.getBoolean("explosions.filter-foreign-claims", true)
        )
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
        val st = settings()
        // creativeBypassActive = config switch AND player actually in creative mode
        val creativeActive = st.creativeBypass && triggerer != null && triggerer.gameMode == org.bukkit.GameMode.CREATIVE
        return shouldCancelClaimEvent(
            chunkClaimed = SMPClaim.dataHandler.isChunkClaimed(chunk),
            flagAllows = check,
            alreadyCancelled = event.isCancelled,
            bypasses = bypasses,
            ownerOrMember = ownerOrMember,
            creativeBypassActive = creativeActive,
            permissionBypassEnabled = st.permissionBypass,
            ownerExplosionBypassEnabled = st.ownClaimExplosion,
            filterForeignClaims = st.filterForeignClaims,
            triggererTriggered = triggerer != null
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
 * Claim-event cancellation decision extracted into a pure function.
 *
 * @param chunkClaimed          does the relevant chunk have an active claim?
 * @param flagAllows            does the config-flag permit this action type?
 * @param alreadyCancelled      has some other handler already cancelled the event?
 * @param bypasses              did the triggerer hold the bypass permission?
 * @param ownerOrMember         is the triggerer owner or member of the claim?
 * @param creativeBypassEnabled when true, creative-mode players are exempt from claims
 * @param ownerExplosionBypassEnabled when true and triggerer-triggered, explosions fired by
 *                                    owners/members of the claim are NOT cancelled
 * @param filterForeignClaims   when true, callers may filter blocks instead of cancelling
 * @param triggererTriggered    whether the event has a recognised player triggerer
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
    filterForeignClaims: Boolean = true,
    triggererTriggered: Boolean = false
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

    // Owner-triggered explosions: if the triggerer owns/is a member AND this is
    // an explosion-type event, skip cancellation when own-claim-explosion is true.
    if (ownerExplosionBypassEnabled && ownerOrMember && triggererTriggered) {
        return false
    }

    return !(!chunkClaimed || alreadyCancelled || flagAllows || effectiveBypass || ownerOrMember)
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
 * Logic:
 * - Wildnis (unclaimed chunks): blocks always survive.
 * - Owned claim: blocks survive if triggerer owns/is member of the claim.
 *   When there is no triggerer (e.g. BlockExplodeEvent), blocks in claimed chunks
 *   are kept because without knowing WHO caused the explosion we can't determine
 *   ownership — conservatively preserving blast effects in claimed areas.
 * - Foreign claim: blocks are removed (the core "filter-foreign-claims" behavior).
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
            // Claimed — check if triggerer owns or has access
            // When no triggerer (BlockExplodeEvent), keep blocks (conservative)
            triggererId == null || hasAccessOrIsOwner(triggererId, blockPos)
        }
    }
}
