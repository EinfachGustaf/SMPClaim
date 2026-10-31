package live.einfachgustaf.smpclaim.listeners

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListenerLogicTest {

    private fun cancel(
        chunkClaimed: Boolean = true,
        flagAllows: Boolean = false,
        alreadyCancelled: Boolean = false,
        bypasses: Boolean = false,
        ownerOrMember: Boolean = false,
        creativeBypassActive: Boolean = false,
        permissionBypassEnabled: Boolean = true,
        ownerExplosionBypassEnabled: Boolean = true,
        filterForeignClaims: Boolean = true,
        triggererTriggered: Boolean = false
    ): Boolean = shouldCancelClaimEvent(
        chunkClaimed, flagAllows, alreadyCancelled, bypasses, ownerOrMember,
        creativeBypassActive, permissionBypassEnabled, ownerExplosionBypassEnabled, filterForeignClaims, triggererTriggered
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

    // --- New tests for configurable protection (Part 1) ---

    /** Creative-mode players are protected by claims just like anyone else
     *  unless bypass-creative is explicitly enabled. */
    @Test
    fun `creative players are protected by claims when bypass creative is false`() {
        assertTrue(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = false,
            triggererTriggered = true
        ))
    }

    @Test
    fun `creative bypass ACTIVE allows creative players through`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = false,
            creativeBypassActive = true, triggererTriggered = true
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
            permissionBypassEnabled = false,
            triggererTriggered = true
        ))
    }

    @Test
    fun `owner-triggered explosion is NOT cancelled when own-claim is true`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = true,
            triggererTriggered = true
        ))
    }

    /** Even with own-claim-explosion disabled, owners/members are still
     *  protected by the ownerOrMember gate — they may act in their claim. */
    @Test
    fun `owner-triggered explosion in own chunk always passes when ownerOrMember is true`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = true,
            ownerExplosionBypassEnabled = false,
            triggererTriggered = true
        ))
    }

    @Test
    fun `non-owner triggered explosion is still cancelled when own-claim is true`() {
        assertTrue(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = false,
            triggererTriggered = true
        ))
    }

    @Test
    fun `flag-allow overrides all other checks`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = true,
            bypasses = false, ownerOrMember = false,
            triggererTriggered = false
        ))
    }

    @Test
    fun `unclaimed chunk overrides creative bypass setting`() {
        assertFalse(cancel(
            chunkClaimed = false, flagAllows = false,
            creativeBypassActive = true, triggererTriggered = true
        ))
    }

    // --- Part 2: filterBlocksInForeignClaims tests ---

    private val CHUNK_OWNED = live.einfachgustaf.smpclaim.chunk.ChunkPosition("world", 0, 0)
    private val CHUNK_FOREIGN = live.einfachgustaf.smpclaim.chunk.ChunkPosition("world", 1, 1)
    private val CHUNK_WILDERNESS = live.einfachgustaf.smpclaim.chunk.ChunkPosition("world", 5, 5)
    private val ORIGIN = live.einfachgustaf.smpclaim.chunk.ChunkPosition("world", 0, 0)
    private val TRIGGERER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

    @Test
    fun `filterBlocksInForeignClaims keeps blocks in own claimed chunk`() {
        val mockIsClaimed: (live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { it == CHUNK_OWNED }
        val mockHasAccess: (UUID, live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { uuid, cp ->
            cp == CHUNK_OWNED && uuid == TRIGGERER_UUID
        }

        val result = filterBlocksInForeignClaims(
            originChunkPos = ORIGIN,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_OWNED),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertEquals(listOf(CHUNK_OWNED), result)
    }

    @Test
    fun `filterBlocksInForeignClaims removes blocks in foreign claim`() {
        val mockIsClaimed: (live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { it == CHUNK_FOREIGN }
        val mockHasAccess: (UUID, live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { _, _ -> false }

        val result = filterBlocksInForeignClaims(
            originChunkPos = ORIGIN,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_FOREIGN),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun `filterBlocksInForeignClaims keeps blocks in wilderness`() {
        val mockIsClaimed: (live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { false }
        val mockHasAccess: (UUID, live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { _, _ -> false }

        val result = filterBlocksInForeignClaims(
            originChunkPos = ORIGIN,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_WILDERNESS),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertEquals(listOf(CHUNK_WILDERNESS), result)
    }

    @Test
    fun `filterBlocksInForeignClaims mixed only foreign removed`() {
        val mockIsClaimed: (live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = {
            it == CHUNK_OWNED || it == CHUNK_FOREIGN
        }
        val mockHasAccess: (UUID, live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { uuid, cp ->
            cp == CHUNK_OWNED && uuid == TRIGGERER_UUID
        }

        val result = filterBlocksInForeignClaims(
            originChunkPos = ORIGIN,
            triggererId = TRIGGERER_UUID,
            blocks = listOf(CHUNK_OWNED, CHUNK_FOREIGN, CHUNK_WILDERNESS),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        assertEquals(listOf(CHUNK_OWNED, CHUNK_WILDERNESS), result)
    }

    @Test
    fun `filterBlocksInForeignClaims no triggerer keeps all claimed chunks conservatively`() {
        val mockIsClaimed: (live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = {
            it == CHUNK_OWNED || it == CHUNK_FOREIGN
        }
        val mockHasAccess: (UUID, live.einfachgustaf.smpclaim.chunk.ChunkPosition) -> Boolean = { _, _ -> false }

        val result = filterBlocksInForeignClaims(
            originChunkPos = ORIGIN,
            triggererId = null,
            blocks = listOf(CHUNK_OWNED, CHUNK_FOREIGN),
            isChunkClaimed = mockIsClaimed,
            hasAccessOrIsOwner = mockHasAccess
        )
        // Without a triggerer, we conservatively keep all claimed-chunk blocks
        assertEquals(listOf(CHUNK_OWNED, CHUNK_FOREIGN), result)
    }
}
