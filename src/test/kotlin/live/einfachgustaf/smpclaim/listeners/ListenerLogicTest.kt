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
        ownerOrMember: Boolean = false
    ): Boolean = shouldCancelClaimEvent(chunkClaimed, flagAllows, alreadyCancelled, bypasses, ownerOrMember)

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
}
