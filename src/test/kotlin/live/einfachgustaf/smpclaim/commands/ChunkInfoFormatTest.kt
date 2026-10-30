package live.einfachgustaf.smpclaim.commands

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class ChunkInfoFormatTest {

    @Test
    fun `unclaimed chunk shows dash placeholders`() {
        val lines = ChunkInfoCommand.formatChunkInfo(
            owner = null, ownerName = null, access = emptyList(), accessNames = emptyList()
        )
        assertEquals(listOf("Chunk Info:", "Owner: -/-", "Access:", "-/-"), lines)
    }

    @Test
    fun `owner and access are listed with their resolved names`() {
        val owner = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val member = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val lines = ChunkInfoCommand.formatChunkInfo(
            owner = owner, ownerName = "Gustaf",
            access = listOf(member), accessNames = listOf("Alice")
        )
        assertEquals(listOf("Chunk Info:", "Owner: Gustaf", "Access:", "- Alice"), lines)
    }

    @Test
    fun `unresolved names fall back to the uuid`() {
        val member = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val lines = ChunkInfoCommand.formatChunkInfo(
            owner = member, ownerName = null, access = listOf(member), accessNames = listOf(null)
        )
        assertEquals(listOf("Chunk Info:", "Owner: $member", "Access:", "- $member"), lines)
    }
}
