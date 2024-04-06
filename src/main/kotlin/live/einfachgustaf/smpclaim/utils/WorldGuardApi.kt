package live.einfachgustaf.smpclaim.utils

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.Flag
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import org.bukkit.Chunk


class WorldGuardApi {

    private lateinit var flagChunkClaim: StateFlag

    fun init(): Boolean {
        flagChunkClaim = StateFlag("chunk-claim", true)
        try {
            WorldGuard.getInstance().flagRegistry.register(flagChunkClaim)
            return true
        } catch (e: FlagConflictException) {
            if (WorldGuard.getInstance().flagRegistry.get("chunk-claim") is StateFlag) {
                flagChunkClaim = WorldGuard.getInstance().flagRegistry.get("chunk-claim") as StateFlag
                return true
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun isAllowedClaim(chunk: Chunk): Boolean {
        try {
            // Generate a region in the given chunk to get all intersecting regions
            val bx = chunk.x shl 4
            val bz = chunk.z shl 4
            val pt1 = BlockVector3.at(bx, 0, bz)
            val pt2 = BlockVector3.at(bx + 15, 256, bz + 15)
            val region = ProtectedCuboidRegion("_", pt1, pt2)
            val regionManager =
                WorldGuard.getInstance()
                    .platform
                    .regionContainer[BukkitAdapter.adapt(chunk.world)]

            // No regions in this world, claiming should be determined by the config
            if (regionManager == null) {
                return true
            }

            // If any regions in the given chunk deny chunk claiming, false is returned
            for (regionIn in regionManager.getApplicableRegions(region)) {
                val flag = regionIn.getFlag<Flag<StateFlag.State>, StateFlag.State>(flagChunkClaim)
                if (flag == StateFlag.State.DENY) return false
            }

            // No objections
            return true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

}