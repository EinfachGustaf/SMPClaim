package live.einfachgustaf.smpclaim.commands

import com.mojang.brigadier.arguments.StringArgumentType
import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import net.axay.kspigot.commands.*
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit

object AccessCommand {

    fun register() {
        command("access") {
            literal("add") {
                argument("player", StringArgumentType.word()) {
                    suggestList {
                        onlinePlayers.map { it.name }
                    }
                    runs {
                        if (SMPClaim.dataHandler.getChunkOwner(ChunkPosition(this.player.chunk)) != this.player.uniqueId) {
                            this.player.sendMessage("You are not the owner of this chunk!")
                            return@runs
                        }
                        Bukkit.getPlayer(this.getArgument<String>("player"))?.let {
                            if (SMPClaim.dataHandler.hasAccessOrIsOwner(it.uniqueId, ChunkPosition(this.player.chunk))) {
                                this.player.sendMessage("Player already has access!")
                                return@runs
                            }
                            SMPClaim.dataHandler.addChunkAccess(ChunkPosition(this.player.chunk), it.uniqueId)
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
                        if (SMPClaim.dataHandler.getChunkOwner(ChunkPosition(this.player.chunk)) != this.player.uniqueId) {
                            this.player.sendMessage("You are not the owner of this chunk!")
                            return@runs
                        }
                        Bukkit.getPlayer(this.getArgument<String>("player"))?.let {
                            if (!SMPClaim.dataHandler.hasAccessOrIsOwner(it.uniqueId, ChunkPosition(this.player.chunk))) {
                                this.player.sendMessage("Player does not have access!")
                                return@runs
                            }
                            if (SMPClaim.dataHandler.getChunkOwner(ChunkPosition(this.player.chunk)) == it.uniqueId) {
                                this.player.sendMessage("You can't remove the access of the owner! Use /unclaim instead.")
                                return@runs
                            }
                            SMPClaim.dataHandler.removeChunkAccess(ChunkPosition(this.player.chunk), it.uniqueId)
                            this.player.sendMessage("Access removed!")
                        } ?: this.player.sendMessage("Player not found!")
                    }
                }
            }
        }
    }

}