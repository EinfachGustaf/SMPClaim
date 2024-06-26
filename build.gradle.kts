plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.22"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "live.einfachgustaf"
version = "1.2"

repositories {
    mavenCentral()

    // EngineHub (WorldGuard)
    maven("https://maven.enginehub.org/repo/")
}

dependencies {

    // Paper
    paperweight.paperDevBundle(libs.versions.paperDevBundle)

    // KSpigot
    compileOnly(libs.kspigot)

    // WorldGuard
    compileOnly(libs.worldguard)

    // Database Drivers
    compileOnly(libs.postgresql)

    // Exposed
    compileOnly(libs.bundles.exposed)

    // kotlinx-serialization
    compileOnly(libs.kotlinxserialization)
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    runServer {
        minecraftVersion("1.20.4")
        downloadPlugins {
            modrinth("worldedit", "JzCMkGax") // WorldEdit
            url("https://dev.bukkit.org/projects/worldguard/files/latest") // WorldGuard
        }
    }

    withType<Jar> {
        archiveFileName.set("smpclaim-$version.jar")
    }
}

kotlin {
    jvmToolchain(17)
}
