package live.einfachgustaf.smpclaim.listeners

import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import java.util.UUID

class ListenerLogicTest {

    // --- Constants for filter tests ---
    private val CHUNK_OWNED = ChunkPosition("world", 0, 0)
    private val CHUNK_FOREIGN = ChunkPosition("world", 1, 0)
    private val CHUNK_WILDERNESS = ChunkPosition("world", 5, 5)
    private val TRIGGERER_UUID = UUID.randomUUID()

    /**
     * Helper for shouldCancelClaimEvent — uses named parameters so callers don't
     * accidentally hit wrong defaults when the signature grows.
     */
    private fun cancel(
        chunkClaimed: Boolean = true,
        flagAllows: Boolean = false,
        alreadyCancelled: Boolean = false,
        bypasses: Boolean = false,
        ownerOrMember: Boolean = false,
        creativeBypassActive: Boolean = false,
        permissionBypassEnabled: Boolean = true,
        ownerExplosionBypassEnabled: Boolean = true,
        isExplosionEvent: Boolean = false
    ): Boolean = shouldCancelClaimEvent(
        chunkClaimed = chunkClaimed,
        flagAllows = flagAllows,
        alreadyCancelled = alreadyCancelled,
        bypasses = bypasses,
        ownerOrMember = ownerOrMember,
        creativeBypassActive = creativeBypassActive,
        permissionBypassEnabled = permissionBypassEnabled,
        ownerExplosionBypassEnabled = ownerExplosionBypassEnabled,
        isExplosionEvent = isExplosionEvent
    )

    @Test
    fun `unclaimed chunks allow everything`() {
        assertFalse(cancel(chunkClaimed = false))
    }

    @Test
    fun `config flag allows the event everywhere`() {
        assertFalse(cancel(flagAllows = true))
    }

    @Test
    fun `owner and members are allowed in their own claim`() {
        assertFalse(cancel(ownerOrMember = true))
    }

    @Test
    fun `bypass permission exempts from claim protection`() {
        assertFalse(cancel(bypasses = true))
    }

    @Test
    fun `events without triggerer are cancelled in claimed chunks when the flag disallows`() {
        assertTrue(cancel())
    }

    @Test
    fun `already cancelled events are not touched`() {
        assertFalse(cancel(alreadyCancelled = true))
    }

    // --- New tests for configurable protection (Part 1: Listeners.kt switches) ---

    /** Creative-mode players are protected by claims just like anyone else
     *  unless bypass-creative is explicitly enabled. */
    @Test
    fun `creative players are protected by claims when bypass creative is false`() {
        assertTrue(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = false,
            isExplosionEvent = true
        ))
    }

    @Test
    fun `creative bypass ACTIVE allows creative players through`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = false,
            creativeBypassActive = true, isExplosionEvent = true
        ))
    }

    @Test
    fun `permission bypass defaults to true (bypass works)`() {
        assertFalse(cancel(bypasses = true))
    }

    /** When the permission-bypass switch is disabled the bypass permission no longer exempts. */
    @Test
    fun `when permission bypass is disabled, permission does NOT exempt`() {
        assertTrue(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = true, ownerOrMember = false,
            permissionBypassEnabled = false, isExplosionEvent = true
        ))
    }

    @Test
    fun `owner-triggered explosion is NOT cancelled when own-claim is true`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = true,
            isExplosionEvent = true
        ))
    }

    /** With own-claim-explosions disabled, even owners/members get their
     *  explosions cancelled in claims — the config switch must be observable. */
    @Test
    fun `owner-triggered explosion is cancelled when own-claim is false`() {
        assertTrue(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = true,
            ownerExplosionBypassEnabled = false, isExplosionEvent = true
        ))
    }

    /** Non-explosion events keep the normal owner-or-member gate, even with
     *  own-claim-explosions disabled. */
    @Test
    fun `non-explosion events keep the owner gate when own-claim is false`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = true,
            ownerExplosionBypassEnabled = false, isExplosionEvent = false
        ))
    }

    @Test
    fun `non-owner triggered explosion is still cancelled when own-claim is true`() {
        assertTrue(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = false,
            isExplosionEvent = true
        ))
    }

    @Test
    fun `flag-allow overrides all other checks`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = true,
            bypasses = false, ownerOrMember = false,
            isExplosionEvent = false
        ))
    }

    @Test
    fun `unclaimed chunk overrides creative bypass setting`() {
        assertFalse(cancel(
            chunkClaimed = false, flagAllows = false,
            creativeBypassActive = true, isExplosionEvent = true
        ))
    }

    // --- Tests for filterBlocksInForeignClaims (Part 2: block-level filtering) ---

    @Test
    fun `filterBlocksInForeignClaims keeps blocks in owned claim with matching triggerer`() {
        val mockIsClaimed: (ChunkPosition) -> Boolean = { it == CHUNK_OWNED }
        val mockHasAccess: (UUID, ChunkPosition) -> Boolean = { uuid, cp ->
            cp == CHUNK_OWNED && uuid == TRIGGERER_UUID
        }

        val result = filterBlocksInForeignClaims(
            originChunkPos = CHUNK_OWNED,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_OWNED),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertEquals(listOf(CHUNK_OWNED), result)
    }

    @Test
    fun `filterBlocksInForeignClaims removes blocks in foreign claim`() {
        val mockIsClaimed: (ChunkPosition) -> Boolean = { it == CHUNK_FOREIGN }
        val mockHasAccess: (UUID, ChunkPosition) -> Boolean = { _, _ -> false }

        val result = filterBlocksInForeignClaims(
            originChunkPos = CHUNK_OWNED,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_FOREIGN),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun `filterBlocksInForeignClaims keeps blocks in wilderness`() {
        val mockIsClaimed: (ChunkPosition) -> Boolean = { false }
        val mockHasAccess: (UUID, ChunkPosition) -> Boolean = { _, _ -> false }

        val result = filterBlocksInForeignClaims(
            originChunkPos = CHUNK_OWNED,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_WILDERNESS),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertEquals(listOf(CHUNK_WILDERNESS), result)
    }

    @Test
    fun `filterBlocksInForeignClaims mixed only foreign removed`() {
        val mockIsClaimed: (ChunkPosition) -> Boolean = {
            it == CHUNK_OWNED || it == CHUNK_FOREIGN
        }
        val mockHasAccess: (UUID, ChunkPosition) -> Boolean = { uuid, cp ->
            cp == CHUNK_OWNED && uuid == TRIGGERER_UUID
        }

        val result = filterBlocksInForeignClaims(
            originChunkPos = CHUNK_OWNED,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_OWNED, CHUNK_FOREIGN, CHUNK_WILDERNESS),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertEquals(listOf(CHUNK_OWNED, CHUNK_WILDERNESS), result)
    }

    /** Without a triggerer we cannot verify ownership → remove ALL blocks in claimed chunks.
     *  Only wilderness survives. This is the conservative safety behavior. */
    @Test
    fun `filterBlocksInForeignClaims no triggerer removes all claimed-chunk blocks keeps wildness`() {
        val mockIsClaimed: (ChunkPosition) -> Boolean = {
            it == CHUNK_OWNED || it == CHUNK_FOREIGN
        }
        val mockHasAccess: (UUID, ChunkPosition) -> Boolean = { _, _ -> false }

        val result = filterBlocksInForeignClaims(
            originChunkPos = CHUNK_OWNED,
            triggererId = null,
            blocks = listOf(CHUNK_OWNED, CHUNK_FOREIGN, CHUNK_WILDERNESS),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertEquals(listOf(CHUNK_WILDERNESS), result)
    }

    /** When a bypass is active (creative or permission), no filtering should occur at all.
     *  The caller decides whether to cancel entirely or let the event pass unchanged. */
    @Test
    fun `filterBlocksInForeignClaims owner as triggerer keeps all claimed blocks`() {
        val mockIsClaimed: (ChunkPosition) -> Boolean = {
            it == CHUNK_OWNED || it == CHUNK_FOREIGN
        }
        val mockHasAccess: (UUID, ChunkPosition) -> Boolean = { uuid, cp ->
            cp == CHUNK_OWNED && uuid == TRIGGERER_UUID
        }

        val result = filterBlocksInForeignClaims(
            originChunkPos = CHUNK_OWNED,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_OWNED, CHUNK_FOREIGN, CHUNK_WILDERNESS),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        // Owner triggers: owns their chunks, foreign is filtered out, wildness stays
        assertEquals(listOf(CHUNK_OWNED, CHUNK_WILDERNESS), result)
    }
}
