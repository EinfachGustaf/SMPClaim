package live.einfachgustaf.smpclaim.utils.configs

import live.einfachgustaf.smpclaim.utils.Config

class DBConfig : Config("database.yml") {
    override fun init() {
        config.addDefault("configVersion", "1.2")
        config.addDefault("type", "POSTGRES")
        config.addDefault("database.url", "jdbc:postgresql://localhost:5432/smpclaim")
        config.addDefault("database.user", "smpclaim")
        config.addDefault("database.password", "someverysecurepassword")
        config.options().copyDefaults(true)
        save()
    }
}