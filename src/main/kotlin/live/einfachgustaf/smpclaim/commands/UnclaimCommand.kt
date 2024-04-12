package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs

object UnclaimCommand {

    fun register() {
        command("unclaim") {
            runs {
                if (this.sender.isPlayer) {
                    if (SMPClaim.dataHandler.getChunkOwner(ChunkPosition(this.player.chunk)) != this.player.uniqueId) {
                        this.player.sendMessage("You do not own this chunk!")
                        return@runs
                    }
                    SMPClaim.dataHandler.removeClaimedChunk(ChunkPosition(this.player.chunk))
                        this.player.sendMessage("Chunk removed!")
                }
            }
        }
    }
}