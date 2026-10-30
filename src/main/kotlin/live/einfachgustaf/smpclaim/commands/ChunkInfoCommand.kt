package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.utils.UUIDFetcher
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

object ChunkInfoCommand {

    fun register() {
        command("cinfo") {
            runs {
                val player: Player = this.player
                val chunk = ChunkPosition(player.chunk)
                val owner = SMPClaim.dataHandler.getChunkOwner(chunk)
                val access = SMPClaim.dataHandler.getChunkAccess(chunk)

                // Mojang lookups are blocking HTTP calls; resolve them off the main thread.
                Bukkit.getScheduler().runTaskAsynchronously(SMPClaim.instance, Runnable {
                    val ownerName = owner?.let { UUIDFetcher.getName(it) }
                    val accessNames = access.map { UUIDFetcher.getName(it) }
                    Bukkit.getScheduler().runTask(SMPClaim.instance, Runnable {
                        formatChunkInfo(owner, ownerName, access, accessNames).forEach(player::sendMessage)
                    })
                })
            }
        }
    }

    /**
     * Pure formatting of the /cinfo output, extracted so it can be unit tested without a server.
     */
    internal fun formatChunkInfo(owner: UUID?, ownerName: String?, access: List<UUID>, accessNames: List<String?>): List<String> {
        val ownerDisplay = if (owner == null) "-/-" else (ownerName ?: owner.toString())
        val lines = mutableListOf("Chunk Info:", "Owner: $ownerDisplay", "Access:")
        if (access.isEmpty()) {
            lines.add("-/-")
            return lines
        }
        access.forEachIndexed { index, uuid -> lines.add("- ${accessNames.getOrNull(index) ?: uuid}") }
        return lines
    }
}
