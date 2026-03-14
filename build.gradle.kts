plugins {
    id("java")
    alias(libs.plugins.userdev)
    alias(libs.plugins.run.paper)
}

group = "live.einfachgustaf"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())
    compileOnly(libs.guice)
}

tasks {
    processResources {
        filesMatching("paper-plugin.yml") {
            expand(
                "version" to project.version
            )
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
