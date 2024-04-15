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
include(":android:user:infra:model")
include(":android:user:infra:network")
include(":android:user:infra:local")
include(":android:user:infra:test")
include(":android:todo:feature:list")
include(":android:todo:infra:network")
include(":android:todo:infra:test")
include(":android:todo:infra:local")
include(":android:infra:db")
include(":android:infra:test")
include(":android:infra:log")
include(":server")
include(":android:todo:feature:create")
include(":android:todo:feature:edit")
include(":android:todo:infra:model")
