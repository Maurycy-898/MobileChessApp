pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "MobileChessApp"

buildCache {
    local {
        directory = File(rootDir, ".build-cache")
    }
}

include(
    ":app",
    ":core:common",
    ":core:compose-ui",
    ":core:model",
    ":core:navigation",
    ":core:repository",
    ":core:strings",
    ":core:theme",
    ":chess:engine",
    ":chess:client",
    ":chess:database",
    ":chess:logic",
    ":chess:model",
    ":chess:server",
    ":chess:ui",
    ":screen:archive",
    ":screen:game",
    ":screen:home",
    ":screen:profile",
    ":screen:settings",
)
