package live.einfachgustaf.smpclaim.data.local

import kotlinx.serialization.json.Json
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.data.local.models.ChunkAccessModel
import live.einfachgustaf.smpclaim.data.local.models.LocalDBModel
import live.einfachgustaf.smpclaim.data.local.models.SerialzableLocation
import java.nio.file.Path
import java.util.*

class LocalDataHandler: IDataHandler {

    lateinit var chunkCache: HashMap<SerialzableLocation, ChunkAccessModel>

    private val json = Json { prettyPrint = true }

    val fileManager = FileManager(
        Path.of("plugins/SMPClaim/localdb.json"),
        json.encodeToString<LocalDBModel>(LocalDBModel(hashMapOf()))
    )

    override fun init() {
        fileManager.setup()
        load()
    }

    override fun exit() {
        save()
    }

    override fun save() {
        fileManager.file.writeText(json.encodeToString<LocalDBModel>(LocalDBModel(chunkCache)))
    }

    override fun load() {
        chunkCache = json.decodeFromString<LocalDBModel>(fileManager.file.readText()).db
    }

    override fun addClaimedChunk(pos: ChunkPosition, player: UUID): Boolean {
        if (chunkCache.containsKey(pos.toSerializableLocation())) return false

        chunkCache[pos.toSerializableLocation()] = ChunkAccessModel(player, arrayListOf())
        return true
    }

    override fun removeClaimedChunk(pos: ChunkPosition) {
        chunkCache.remove(pos.toSerializableLocation())
    }

    override fun isChunkClaimed(pos: ChunkPosition): Boolean {
        return chunkCache.containsKey(pos.toSerializableLocation())
    }

    override fun getChunkOwner(pos: ChunkPosition): UUID? {
        return getChunkData(pos)?.owner
    }

    override fun addChunkAccess(pos: ChunkPosition, player: UUID) {
        getChunkData(pos)?.access?.add(player)
    }

    override fun removeChunkAccess(pos: ChunkPosition, player: UUID) {
        getChunkData(pos)?.access?.remove(player)
    }

    override fun hasAccessOrIsOwner(player: UUID, chunk: ChunkPosition): Boolean {
        val query = getChunkData(chunk) ?: return false

        if (query.owner == player) return true
        if (query.access.contains(player)) return true
        return false
    }

    override fun getChunkAccess(chunk: ChunkPosition): List<UUID> {
        return getChunkData(chunk)?.access ?: listOf()
    }
    
    /**
     * Helper to retrieve chunk data from cache.
     */
    private fun getChunkData(pos: ChunkPosition): ChunkAccessModel? {
        return chunkCache[pos.toSerializableLocation()]
    }

    private fun ChunkPosition.toSerializableLocation(): SerialzableLocation {
        return SerialzableLocation(world, x, z)
    }
}