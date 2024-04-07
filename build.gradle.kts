plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "live.einfachgustaf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    // EngineHub (WorldGuard)
    maven("https://maven.enginehub.org/repo/")
}

dependencies {

    // Paper
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    // KSpigot
    compileOnly("net.axay:kspigot:1.20.3")

    // WorldGuard
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")

    // Database Drivers
    implementation("org.postgresql:postgresql:42.7.3")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:0.35.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.35.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.35.1")
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
}

kotlin {
    jvmToolchain(17)
}
