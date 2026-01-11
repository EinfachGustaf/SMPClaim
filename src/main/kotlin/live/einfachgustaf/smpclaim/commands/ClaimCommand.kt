package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.data.IDataHandler
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClaimCommand @Inject constructor(
    private val dataHandler: IDataHandler
) {

    fun register() {
        command("claim") {
            runs {
                if (dataHandler.addClaimedChunk(ChunkPosition(this.player.chunk), this.player.uniqueId)) {
                    this.player.sendMessage("Chunk claimed!")
                } else {
                    this.player.sendMessage("Chunk already claimed!")
                }
            }
        }
    }

}