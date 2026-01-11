package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs

object UnclaimCommand {

    fun register() {
        command("unclaim") {
            runs {
                val currentChunk = ChunkPosition(this.player.chunk)
                if (SMPClaim.dataHandler.getChunkOwner(currentChunk) != this.player.uniqueId) {
                    this.player.sendMessage("You do not own this chunk!")
                    return@runs
                }
                SMPClaim.dataHandler.removeClaimedChunk(currentChunk)
                    this.player.sendMessage("Chunk removed!")
            }
        }
    }
}