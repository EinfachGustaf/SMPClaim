package live.einfachgustaf.smpclaim.chunk

import org.bukkit.Chunk

class ChunkPosition {

    val x: Int
    val z: Int
    val world: String

    constructor(world: String?, x: Int, z: Int) {
        this.x = x
        this.z = z
        this.world = world ?: "world"
    }

    constructor(chunk: Chunk) {
        this.x = chunk.x
        this.z = chunk.z
        this.world = chunk.world.name
    }

}