package live.einfachgustaf.smpclaim.data

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.data.exposed.dao.ChunkAccess
import live.einfachgustaf.smpclaim.data.exposed.dao.ClaimedChunks
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PostgresDataHandler : IDataHandler {

    private lateinit var database: Database

    /**
     * Initialize the data handler.
     *
     * @throws Exception If an error occurs during initialization.
     */
    override fun init() {
        database = Database.connect(
            url = SMPClaim.dbConfig.config.getString("database.url")!!,
            driver = "org.postgresql.Driver",
            user = SMPClaim.dbConfig.config.getString("database.user")!!,
            password = SMPClaim.dbConfig.config.getString("database.password")!!
        )

        transaction {
            try {
                SchemaUtils.createMissingTablesAndColumns(ClaimedChunks)
                SchemaUtils.createMissingTablesAndColumns(ChunkAccess)
            } catch (e: Exception) {
                throw Exception("Error while creating tables: ${e.message}")
            }
        }
    }

    /**
     * Cleans up anything that needs to be cleaned up before the plugin exits.
     *
     * @throws Exception If an error occurs during exit.
     */
    override fun exit() {
        // Not needed because Exposed handles the connection pool internally
    }

    /**
     * Save the data to the data source.
     *
     * @throws Exception If an error occurs during saving.
     */
    override fun save() {
        // Not needed because SQL data is saved immediately
    }

    /**
     * Load the data from the data source.
     *
     * @throws Exception If an error occurs during loading.
     */
    override fun load() {
        // Not needed because SQL data is loaded immediately
    }

    /**
     * Add a claimed chunk to the data handler.
     *
     * @param pos The position of the chunk.
     * @param player The player who claimed the chunk.
     * @return `true` if the chunk was successfully added, `false` if the chunk was already claimed.
     */
    override fun addClaimedChunk(pos: ChunkPosition, player: UUID): Boolean {
        if (transaction {
            ClaimedChunks.select {
                ClaimedChunks.world.eq(pos.world)
                    .and(ClaimedChunks.x.eq(pos.x))
                    .and(ClaimedChunks.z.eq(pos.z))
            }.count() > 0
        }) {

            // Return false if the chunk is already claimed
            return false

        }

        if (!SMPClaim.worldGuardApi.isAllowedClaim(Bukkit.getWorld(pos.world)!!.getChunkAt(pos.x, pos.z))) {
            // Return false if the chunk is not allowed to be claimed because regions are denying it
            return false
        }

        transaction {
            ClaimedChunks.insert {
                it[world] = pos.world
                it[x] = pos.x
                it[z] = pos.z
                it[owner] = player
            }
        }
        return true
    }

    /**
     * Remove a claimed chunk from the data handler. Does nothing if the chunk is not claimed.
     *
     * @param pos The position of the chunk.
     */
    override fun removeClaimedChunk(pos: ChunkPosition) {
        transaction {
            ChunkAccess.deleteWhere { ChunkAccess.chunk.eq(
                ClaimedChunks.select {
                    ClaimedChunks.world.eq(pos.world)
                        .and(ClaimedChunks.x.eq(pos.x))
                        .and(ClaimedChunks.z.eq(pos.z))
                }.single()[ClaimedChunks.id]
            ) }
            ClaimedChunks.deleteWhere {
                ClaimedChunks.world.eq(pos.world)
                    .and(ClaimedChunks.x.eq(pos.x))
                    .and(ClaimedChunks.z.eq(pos.z))
            }
        }
    }

    /**
     * Check if a chunk is claimed.
     *
     * @param pos The position of the chunk.
     * @return `true` if the chunk is claimed, `false` if the chunk is not claimed.
     */
    override fun isChunkClaimed(pos: ChunkPosition): Boolean {
        return transaction {
            ClaimedChunks.select {
                ClaimedChunks.world.eq(pos.world)
                    .and(ClaimedChunks.x.eq(pos.x))
                    .and(ClaimedChunks.z.eq(pos.z))
            }.count() > 0
        }
    }

    /**
     * Get the owner of a claimed chunk.
     *
     * @param pos The position of the chunk.
     * @return The UUID of the player who owns the chunk, or `null` if the chunk is not claimed.
     */
    override fun getChunkOwner(pos: ChunkPosition): UUID? {
        if (!isChunkClaimed(pos)) return null
        return transaction {
            ClaimedChunks.select {
                ClaimedChunks.world.eq(pos.world)
                    .and(ClaimedChunks.x.eq(pos.x))
                    .and(ClaimedChunks.z.eq(pos.z))
            }.singleOrNull()?.get(ClaimedChunks.owner)
        }
    }

    /**
     * Add access to a claimed chunk for a player.
     */
    override fun addChunkAccess(pos: ChunkPosition, player: UUID) {
        return transaction {
            ChunkAccess.insert {
                it[chunk] = ClaimedChunks.select {
                    ClaimedChunks.world.eq(pos.world)
                        .and(ClaimedChunks.x.eq(pos.x))
                        .and(ClaimedChunks.z.eq(pos.z))
                }.single()[ClaimedChunks.id]
                it[uuid] = player
            }
        }
    }

    /**
     * Remove access to a claimed chunk for a player.
     */
    override fun removeChunkAccess(pos: ChunkPosition, player: UUID) {
        return transaction {
            ChunkAccess.deleteWhere {
                ChunkAccess.chunk.eq(
                    ClaimedChunks.select {
                        ClaimedChunks.world.eq(pos.world)
                            .and(ClaimedChunks.x.eq(pos.x))
                            .and(ClaimedChunks.z.eq(pos.z))
                    }.single()[ClaimedChunks.id]
                ).and(ChunkAccess.uuid.eq(player))
            }
        }
    }

    /**
     * Check if a player has access to a claimed chunk or is its owner.
     */
    override fun hasAccessOrIsOwner(player: UUID, chunk: ChunkPosition): Boolean {
        if (getChunkOwner(chunk) == player) return true
        return transaction {
            ChunkAccess.select {
                ChunkAccess.chunk.eq(
                    ClaimedChunks.select {
                        ClaimedChunks.world.eq(chunk.world)
                            .and(ClaimedChunks.x.eq(chunk.x))
                            .and(ClaimedChunks.z.eq(chunk.z))
                    }.single()[ClaimedChunks.id]
                ).and(ChunkAccess.uuid.eq(player))
            }.count() > 0
        }
    }
}