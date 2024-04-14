package live.einfachgustaf.smpclaim.data.local

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.data.local.models.ChunkAccessModel
import live.einfachgustaf.smpclaim.data.local.models.LocalDBModel
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap

class LocalDataHandler: IDataHandler {

    lateinit var chunkCache: HashMap<Pair<Int, Int>, ChunkAccessModel>

    private val json = Json { prettyPrint = true }

    val fileManager = FileManager(
        Path.of("plugins/SMPClaim/localdb.json"),
        json.encodeToString<LocalDBModel>(LocalDBModel(hashMapOf()))
    )

    override fun init() {
        val fileManager = FileManager(
            Path.of("plugins/SMPClaim/localdb.json"),
            json.encodeToString<LocalDBModel>(LocalDBModel(hashMapOf()))
        )
        fileManager.setup()

        chunkCache = json.decodeFromString<LocalDBModel>(fileManager.file.readText()).db
    }

    override fun exit() {
        TODO("Not yet implemented")
    }

    override fun save() {
        fileManager.file.writeText(json.encodeToString<LocalDBModel>(LocalDBModel(hashMapOf())))
    }

    override fun load() {
        TODO("Not yet implemented")
    }

    override fun addClaimedChunk(pos: ChunkPosition, player: UUID): Boolean {
        if (chunkCache.containsKey(pos.toPair())) return false

        chunkCache[pos.toPair()] = ChunkAccessModel(player, arrayListOf())
        return true
    }

    override fun removeClaimedChunk(pos: ChunkPosition) {
        chunkCache.remove(pos.toPair())
    }

    override fun isChunkClaimed(pos: ChunkPosition): Boolean {
        return chunkCache.containsKey(pos.toPair())
    }

    override fun getChunkOwner(pos: ChunkPosition): UUID? {
        val query = chunkCache[pos.toPair()] ?: return null

        return query.owner
    }

    override fun addChunkAccess(pos: ChunkPosition, player: UUID) {
        val query = chunkCache[pos.toPair()] ?: return

        query.access.add(player)
    }

    override fun removeChunkAccess(pos: ChunkPosition, player: UUID) {
        val query = chunkCache[pos.toPair()] ?: return

        query.access.remove(player)
    }

    override fun hasAccessOrIsOwner(player: UUID, chunk: ChunkPosition): Boolean {
        val query = chunkCache[chunk.toPair()]?: return false

        if (query.owner == player) return true
        if (query.access.contains(player)) return true
        return false
    }

    override fun getChunkAccess(chunk: ChunkPosition): List<UUID> {
        val query = chunkCache[chunk.toPair()]?: return listOf()

        return query.access
    }

    private fun ChunkPosition.toPair(): Pair<Int, Int> {
        return Pair(x, z)
    }
}