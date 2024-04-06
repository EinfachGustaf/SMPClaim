package live.einfachgustaf.smpclaim.data.exposed.dao

import org.jetbrains.exposed.dao.id.IntIdTable

object ClaimedChunks : IntIdTable() {

    val x = integer("x")
    val z = integer("z")
    val world = varchar("world", 255)
    val owner = uuid("owner")

}