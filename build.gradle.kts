plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.run.paper)
}

group = "live.einfachgustaf"
version = "1.2"

repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/") // EngineHub (WorldGuard)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.kspigot)
    compileOnly(libs.kotlinxserialization)

    compileOnly(libs.worldguard) {
        exclude(group = "com.google.guava")
        exclude(group = "com.google.code.gson")
        exclude(group = "it.unimi.dsi")
    }
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        downloadPlugins {
            modrinth("worldedit", libs.versions.worldedit.get()) // WorldEdit
            url("https://ci.enginehub.org/repository/download/bt11/28376:id/worldguard-bukkit-7.0.16-SNAPSHOT-dist.jar?branch=version/7.0.x&guest=1") // WorldGuard
        }
    }

    withType<Jar> {
        archiveFileName.set("smpclaim-$version.jar")
    }

    processResources {
        filesMatching("paper-plugin.yml") {
            expand(
                "version" to project.version
            )
        }
    }
}

kotlin {
    jvmToolchain(21)
}
