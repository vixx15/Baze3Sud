rootProject.name = "sud"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google() // needed for Compose
        mavenCentral()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // CMP repo
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":frontend")
include(":backend")
include(":shared")


