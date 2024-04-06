package live.einfachgustaf.smpclaim.utils

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

abstract class Config(name: String) {
    private val file: File
    val config: YamlConfiguration

    init {
        val dir = File("./plugins/SMPClaim/")
        if (!dir.exists()) {
            val created = dir.mkdirs()
            if (created) Bukkit.getConsoleSender().sendMessage("Created directory: " + dir.absolutePath)
        }
        file = File(dir, name)
        if (!file.exists()) {
            try {
                val created = file.createNewFile()
                if (created) Bukkit.getConsoleSender().sendMessage("Created file: " + file.absolutePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        try {
            config.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    abstract fun init()
}