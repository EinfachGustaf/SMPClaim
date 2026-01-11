package live.einfachgustaf.smpclaim.commands

import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.data.IDataHandler
import live.einfachgustaf.smpclaim.utils.UUIDFetcher
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChunkInfoCommand @Inject constructor(
    private val dataHandler: IDataHandler
) {

    fun register() {
        command("cinfo") {
            runs {
                val chunk = ChunkPosition(this.player.chunk)
                val owner = dataHandler.getChunkOwner(chunk)
                val ownerString = if (owner != null) UUIDFetcher.getName(owner) else "-/-"
                val access = dataHandler.getChunkAccess(chunk)
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