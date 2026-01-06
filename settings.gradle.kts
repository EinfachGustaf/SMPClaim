rootProject.name = "smpclaim"

pluginManagement {
    repositories {
        gradlePluginPortal()
        // PaperMC Repo
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            // Versions
            version("kspigot", "1.21.0")
            version("worldguard", "7.0.9")
            version("postgresql", "42.7.8")
            version("exposed", "0.35.1")
            version("kotlinx-serialization", "1.9.0")
            version("paperDevBundle", "1.21.11-R0.1-SNAPSHOT")

            // Libraries (alias, group, artifact)
            library("kspigot", "net.axay", "kspigot").versionRef("kspigot")
            library("worldguard", "com.sk89q.worldguard", "worldguard-bukkit").versionRef("worldguard")
            library("postgresql", "org.postgresql", "postgresql").versionRef("postgresql")
            library("kotlinxserialization", "org.jetbrains.kotlinx", "kotlinx-serialization-json")
                .versionRef("kotlinx-serialization")

            library("exposed-core", "org.jetbrains.exposed", "exposed-core").versionRef("exposed")
            library("exposed-dao", "org.jetbrains.exposed", "exposed-dao").versionRef("exposed")
            library("exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef("exposed")

            // Bundle
            bundle("exposed", listOf("exposed-core", "exposed-dao", "exposed-jdbc"))
        }
    }
}
