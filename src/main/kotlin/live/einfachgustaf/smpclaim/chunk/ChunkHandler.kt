package live.einfachgustaf.smpclaim.chunk

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.data.IDataHandler
import java.util.*


class ChunkHandler {

    var dataHandler: IDataHandler = SMPClaim.dataHandler

    /**
     * Claims a specific chunk for a player if that chunk is not already owned.
     *
     * @param world The current world.
     * @param x The chunk x-coord.
     * @param z The chunk z-coord.
     * @param player The player for whom to claim the chunk.
     * @return The chunk position variable or `null` if the chunk is already claimed
     */
    fun claimChunk(world: String?, x: Int, z: Int, player: UUID): ChunkPosition? {
        if (isClaimed(world, x, z)) {
            // If the chunk is already claimed, return null
            return null
        }

        // Create a chunk position representation
        val pos = ChunkPosition(world, x, z)

        // Add the chunk to the claimed chunk and return the position or null if the chunk is already claimed
        val successful = dataHandler.addClaimedChunk(pos, player)
        dataHandler.save()
        return if (successful) pos else null

    }

    fun isClaimed(world: String?, x: Int, z: Int): Boolean {
        return dataHandler.isChunkClaimed(ChunkPosition(world, x, z))
    }

}