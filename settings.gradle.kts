rootProject.name = "SMPClaim"

dependencyResolutionManagement {
    versionCatalogs.create("libs") {
        version("paper", "1.21.11-R0.1-SNAPSHOT")
        version("userdev", "2.0.0-beta.19")
        version("run-paper", "3.0.2")
        version("guice", "7.0.0")

        library("guice", "com.google.inject", "guice").versionRef("guice")

        plugin("userdev", "io.papermc.paperweight.userdev").versionRef("userdev")
        plugin("run-paper", "xyz.jpenilla.run-paper").versionRef("run-paper")
    }
}
