plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}

android {
    namespace = "com.ring.ring"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.ring.ring"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    dependencies {
        implementation(projects.android.todo.feature.list)
        implementation(projects.android.user.feature.login)
        implementation(projects.android.user.feature.signup)

        implementation(libs.bundles.compose)
        implementation(libs.bundles.hilt)
        configurations.getByName("kapt").dependencies.add(
            libs.hilt.compiler.get()
        )

        debugImplementation(libs.bundles.compose.ui.tool)
    }
}

kapt {
    correctErrorTypes = true
}

tasks.register("allTests") {
    dependsOn(
        "testDebugUnitTest",
        "todo:feature:list:testDebugUnitTest",
        "todo:infra:network:testDebugUnitTest",
        "todo:infra:local:testDebugUnitTest",
        "user:feature:login:testDebugUnitTest",
        "user:feature:signup:testDebugUnitTest",
        "user:infra:network:testDebugUnitTest",
        "user:infra:local:testDebugUnitTest",
//        "connectedAndroidTest",
    )
}