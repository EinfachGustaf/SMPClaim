package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.utils.UUIDFetcher
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs

object ChunkInfoCommand {

    fun register() {
        command("cinfo") {
            runs {
                val chunk = ChunkPosition(this.player.chunk)
                val owner = SMPClaim.dataHandler.getChunkOwner(chunk)
                val ownerString = if (owner != null) UUIDFetcher.getName(owner) else "-/-"
                val access = SMPClaim.dataHandler.getChunkAccess(chunk)
                this.player.sendMessage("Chunk Info:")
                this.player.sendMessage("Owner: $ownerString")
                this.player.sendMessage("Access:")
                if (access.isEmpty()) {
                    this.player.sendMessage("-/-")
                    return@runs
                }
                access.forEach {
                    this.player.sendMessage("- ${UUIDFetcher.getName(it) ?: it}")
                }
            }
        }
    }

}