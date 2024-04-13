package live.einfachgustaf.smpclaim.data

import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import java.util.UUID

interface IDataHandler {

    // --- DATA --- //

    /**
     * Initialize the data handler.
     *
     * @throws Exception If an error occurs during initialization.
     */
    @Throws(Exception::class)
    fun init()

    /**
     * Cleans up anything that needs to be cleaned up before the plugin exits.
     *
     * @throws Exception If an error occurs during exit.
     */
    @Throws(Exception::class)
    fun exit()

    /**
     * Save the data to the data source.
     *
     * @throws Exception If an error occurs during saving.
     */
    @Throws(Exception::class)
    fun save()

    /**
     * Load the data from the data source.
     *
     * @throws Exception If an error occurs during loading.
     */
    @Throws(Exception::class)
    fun load()

    // --- CHUNKS --- //

    /**
     * Add a claimed chunk to the data handler.
     *
     * @param pos The position of the chunk.
     * @param player The player who claimed the chunk.
     * @return `true` if the chunk was successfully added, `false` if the chunk was already claimed.
     */
    fun addClaimedChunk(pos: ChunkPosition, player: UUID): Boolean

    /**
     * Remove a claimed chunk from the data handler. Does nothing if the chunk is not claimed.
     *
     * @param pos The position of the chunk.
     */
    fun removeClaimedChunk(pos: ChunkPosition)

    /**
     * Check if a chunk is claimed.
     *
     * @param pos The position of the chunk.
     * @return `true` if the chunk is claimed, `false` if the chunk is not claimed.
     */
    fun isChunkClaimed(pos: ChunkPosition): Boolean

    /**
     * Get the owner of a claimed chunk.
     *
     * @param pos The position of the chunk.
     * @return The UUID of the player who owns the chunk, or `null` if the chunk is not claimed.
     */
    fun getChunkOwner(pos: ChunkPosition): UUID?

    /**
     * Add access to a claimed chunk for a player.
     */
    fun addChunkAccess(pos: ChunkPosition, player: UUID)

    /**
     * Remove access to a claimed chunk for a player.
     */
    fun removeChunkAccess(pos: ChunkPosition, player: UUID)

    /**
     * Check if a player has access to a claimed chunk or is its owner.
     */
    fun hasAccessOrIsOwner(player: UUID, chunk: ChunkPosition): Boolean

    /**
     * Get all players who have access to a claimed chunk.
     */
    fun getChunkAccess(chunk: ChunkPosition): List<UUID>

}