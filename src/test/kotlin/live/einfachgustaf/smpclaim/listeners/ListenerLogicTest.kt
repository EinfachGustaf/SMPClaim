package live.einfachgustaf.smpclaim.listeners

import kotlin.test.Test
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

    // --- New tests for configurable protection (Part 1) ---

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

    /** Even with own-claim-explosion disabled, owners/members are still
     *  protected by the ownerOrMember gate — they may act in their claim. */
    @Test
    fun `owner-triggered explosion in own chunk always passes when ownerOrMember is true`() {
        assertFalse(cancel(
            chunkClaimed = true, flagAllows = false,
            bypasses = false, ownerOrMember = true,
            ownerExplosionBypassEnabled = false, isExplosionEvent = true
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
}
