package live.einfachgustaf.smpclaim.commands

import com.mojang.brigadier.arguments.StringArgumentType
import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.commands.*
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object AccessCommand {

    fun register() {
        command("access") {
            literal("add") {
                argument("player", StringArgumentType.word()) {
                    suggestList {
                        onlinePlayers.map { it.name }
                    }
                    runs {
                        val currentChunk = ChunkPosition(this.player.chunk)
                        if (!isOwner(this.player, currentChunk)) {
                            this.player.sendMessage("You are not the owner of this chunk!")
                            return@runs
                        }
                        Bukkit.getPlayer(this.getArgument<String>("player"))?.let {
                            if (SMPClaim.dataHandler.hasAccessOrIsOwner(it.uniqueId, currentChunk)) {
                                this.player.sendMessage("Player already has access!")
                                return@runs
                            }
                            SMPClaim.dataHandler.addChunkAccess(currentChunk, it.uniqueId)
                            this.player.sendMessage("Access granted!")
                        } ?: this.player.sendMessage("Player not found!")
                    }
                }
            }
            literal("remove") {
                argument("player", StringArgumentType.word()) {
                    suggestList {
                        onlinePlayers.map { it.name }
                    }
                    runs {
                        val currentChunk = ChunkPosition(this.player.chunk)
                        if (!isOwner(this.player, currentChunk)) {
                            this.player.sendMessage("You are not the owner of this chunk!")
                            return@runs
                        }
                        Bukkit.getPlayer(this.getArgument<String>("player"))?.let {
                            if (!SMPClaim.dataHandler.hasAccessOrIsOwner(it.uniqueId, currentChunk)) {
                                this.player.sendMessage("Player does not have access!")
                                return@runs
                            }
                            if (isOwner(it, currentChunk)) {
                                this.player.sendMessage("You can't remove the access of the owner! Use /unclaim instead.")
                                return@runs
                            }
                            SMPClaim.dataHandler.removeChunkAccess(currentChunk, it.uniqueId)
                            this.player.sendMessage("Access removed!")
                        } ?: this.player.sendMessage("Player not found!")
                    }
                }
            }
        }
    }

    private fun isOwner(player: Player, chunk: ChunkPosition): Boolean {
        return SMPClaim.dataHandler.getChunkOwner(chunk) == player.uniqueId
    }

}