rootProject.name = "Android-Experiment"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":android")
include(":android:user:feature:login")
include(":android:user:feature:signup")
include(":android:user:infra:network")
include(":android:user:infra:local")
include(":android:user:infra:test")
include(":android:infra:test")
include(":server")
include(":android:infra:log")
