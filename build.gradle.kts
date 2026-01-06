plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "1.9.22"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("xyz.jpenilla.run-paper") version "3.0.2"
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
        minecraftVersion("1.21.11")
        downloadPlugins {
            modrinth("worldedit", "XlUIRmF8") // WorldEdit
            url("https://dev.bukkit.org/projects/worldguard/files/latest") // WorldGuard
        }
    }

    withType<Jar> {
        archiveFileName.set("smpclaim-$version.jar")
    }
}

kotlin {
    jvmToolchain(21)
}
