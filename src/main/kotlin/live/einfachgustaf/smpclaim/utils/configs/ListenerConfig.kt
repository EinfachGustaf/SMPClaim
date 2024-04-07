package live.einfachgustaf.smpclaim.utils.configs

import live.einfachgustaf.smpclaim.utils.Config

class ListenerConfig : Config("listener.yml") {
    override fun init() {
        config.addDefault("message", "§cDas kannst du hier nicht machen.")
        config.addDefault("canEntityInteraction", false)
        config.addDefault("canEntityDamage", false)
        config.addDefault("canCreatureSpawn", false)
        config.addDefault("canVehicleEntityCollision", false)
        config.addDefault("canBlockBreak", false)
        config.addDefault("canChangeBlock", false)
        config.addDefault("canBlockPlace", false)
        config.addDefault("canPlayerInteract", false)
        config.addDefault("canProjectileLaunch", false)
        config.addDefault("canHangingBreakByEntity", false)
        config.addDefault("canHangingPlace", false)
        config.addDefault("canPlayerBucketFill", false)
        config.addDefault("canPlayerBucketEmpty", false)
        config.addDefault("canPlayerBucketEntity", false)
        config.addDefault("canPlayerLeashEntity", false)
        config.addDefault("canPlayerUnleashEntity", false)
        config.addDefault("canPlayerArmorStandManipulate", false)
        config.addDefault("canBlockExplode", false)
        config.addDefault("canEntityExplode", false)
        config.addDefault("canBlockSpread", false)
        config.addDefault("canBlockPistonExtend", false)
        config.addDefault("canBlockPistonRetract", false)
        config.addDefault("canBlockFertilize", false)
        config.options().copyDefaults(true)
        save()
    }
}