package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs

object ClaimCommand {

    const val PERMISSION = "smpclaim.claim"

    fun register() {
        command("claim") {
            requiresPermission(PERMISSION)
            runs {
                if (SMPClaim.dataHandler.addClaimedChunk(ChunkPosition(this.player.chunk), this.player.uniqueId)) {
                    this.player.sendMessage("Chunk claimed!")
                } else {
                    this.player.sendMessage("Chunk already claimed!")
                }
            }
        }
    }

}