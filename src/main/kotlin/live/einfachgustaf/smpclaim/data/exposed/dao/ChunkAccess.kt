package live.einfachgustaf.smpclaim.data.exposed.dao

import org.jetbrains.exposed.dao.id.IntIdTable

object ChunkAccess : IntIdTable() {
    val uuid = uuid("player")
    val chunk = reference("chunk", ClaimedChunks)
}