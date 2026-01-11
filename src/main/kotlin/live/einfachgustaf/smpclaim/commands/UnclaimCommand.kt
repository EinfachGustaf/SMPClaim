package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.data.IDataHandler
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnclaimCommand @Inject constructor(
    private val dataHandler: IDataHandler
) {

    fun register() {
        command("unclaim") {
            runs {
                if (dataHandler.getChunkOwner(ChunkPosition(this.player.chunk)) != this.player.uniqueId) {
                    this.player.sendMessage("You do not own this chunk!")
                    return@runs
                }
                dataHandler.removeClaimedChunk(ChunkPosition(this.player.chunk))
                    this.player.sendMessage("Chunk removed!")
            }
        }
    }
}