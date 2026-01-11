package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs

object ClaimCommand {

    fun register() {
        command("claim") {
            runs {
                println("isPlayer")
                val currentChunk = ChunkPosition(this.player.chunk)
                if (SMPClaim.dataHandler.addClaimedChunk(currentChunk, this.player.uniqueId)) {
                    println("claim")
                    this.player.sendMessage("Chunk claimed!")
                } else {
                    println("alreadyClaimed")
                    this.player.sendMessage("Chunk already claimed!")
                }
            }
        }
    }

}