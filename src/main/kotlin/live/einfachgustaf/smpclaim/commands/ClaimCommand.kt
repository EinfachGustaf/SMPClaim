package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs

object ClaimCommand {

    fun register() {
        command("claim") {
            runs {
                if (this.sender.isPlayer) {
                    if (SMPClaim.dataHandler.addClaimedChunk(ChunkPosition(this.player.chunk), this.player.uniqueId)) {
                        this.player.sendMessage("Chunk claimed!")
                    } else {
                        this.player.sendMessage("Chunk already claimed!")
                    }
                }
            }
        }
    }

}