package live.einfachgustaf.smpclaim.commands

import com.mojang.brigadier.arguments.StringArgumentType
import live.einfachgustaf.smpclaim.SMPClaim
import live.einfachgustaf.smpclaim.chunk.ChunkPosition
import live.einfachgustaf.smpclaim.utils.UUIDFetcher
import net.axay.kspigot.commands.*
import net.axay.kspigot.extensions.onlinePlayers
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

object AccessCommand {

    fun register() {
        command("access") {
            literal("add") {
                argument("player", StringArgumentType.word()) {
                    suggestList {
                        onlinePlayers.map { it.name }
                    }
                    runs {
                        val player: Player = this.player
                        val targetName = this.getArgument<String>("player")
                        resolveTarget(player, targetName) { grantAccess(player, it) }
                    }
                }
            }
            literal("remove") {
                argument("player", StringArgumentType.word()) {
                    suggestList {
                        onlinePlayers.map { it.name }
                    }
                    runs {
                        val player: Player = this.player
                        val targetName = this.getArgument<String>("player")
                        resolveTarget(player, targetName) { revokeAccess(player, it) }
                    }
                }
            }
        }
    }

    /**
     * Resolves the target player: online players directly, offline names asynchronously via UUIDFetcher.
     * The callback always runs on the main server thread.
     */
    private fun resolveTarget(requester: Player, name: String, onResolved: (UUID) -> Unit) {
        val online = Bukkit.getPlayer(name)
        if (online != null) {
            onResolved(online.uniqueId)
            return
        }
        UUIDFetcher.getUUID(name) { uuid ->
            Bukkit.getScheduler().runTask(SMPClaim.instance, Runnable {
                if (uuid == null) requester.sendMessage("Player not found!")
                else onResolved(uuid)
            })
        }
    }

    private fun grantAccess(requester: Player, target: UUID) {
        val chunk = ChunkPosition(requester.chunk)
        if (SMPClaim.dataHandler.getChunkOwner(chunk) != requester.uniqueId) {
            requester.sendMessage("You are not the owner of this chunk!")
            return
        }
        if (SMPClaim.dataHandler.hasAccessOrIsOwner(target, chunk)) {
            requester.sendMessage("Player already has access!")
            return
        }
        SMPClaim.dataHandler.addChunkAccess(chunk, target)
        requester.sendMessage("Access granted!")
    }

    private fun revokeAccess(requester: Player, target: UUID) {
        val chunk = ChunkPosition(requester.chunk)
        if (SMPClaim.dataHandler.getChunkOwner(chunk) != requester.uniqueId) {
            requester.sendMessage("You are not the owner of this chunk!")
            return
        }
        if (!SMPClaim.dataHandler.hasAccessOrIsOwner(target, chunk)) {
            requester.sendMessage("Player does not have access!")
            return
        }
        if (SMPClaim.dataHandler.getChunkOwner(chunk) == target) {
            requester.sendMessage("You can't remove the access of the owner! Use /unclaim instead.")
            return
        }
        SMPClaim.dataHandler.removeChunkAccess(chunk, target)
        requester.sendMessage("Access removed!")
    }
}
