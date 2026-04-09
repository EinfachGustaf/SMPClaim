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
            version("minecraft", "1.21.11")
            version("worldedit", "XlUIRmF8")

            version("kotlin", "2.3.0")
            version("kotlinx-serialization-plugin", "1.9.0")
            version("paperweight-userdev", "2.0.0-SNAPSHOT")
            version("run-paper", "3.0.2")

            version("paper", "1.21.11-R0.1-SNAPSHOT")

            version("kspigot", "1.21.0")
            version("worldguard", "7.0.15")
            version("kotlinx-serialization", "1.11.0")
            version("configurate", "4.2.0")

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlinx-serialization-plugin", "org.jetbrains.kotlin.plugin.serialization")
                .versionRef("kotlinx-serialization-plugin")
            plugin("paperweight-userdev", "io.papermc.paperweight.userdev").versionRef("paperweight-userdev")
            plugin("run-paper", "xyz.jpenilla.run-paper").versionRef("run-paper")

            library("kspigot", "net.axay", "kspigot").versionRef("kspigot")
            library("worldguard", "com.sk89q.worldguard", "worldguard-bukkit").versionRef("worldguard")
            library("kotlinx-serialization", "org.jetbrains.kotlinx", "kotlinx-serialization-json")
                .versionRef("kotlinx-serialization")
            library("configurate", "org.spongepowered", "configurate-hocon").versionRef("configurate")
        }
    }
}
