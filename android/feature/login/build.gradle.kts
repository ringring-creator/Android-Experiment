plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ring.ring.login"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        buildConfigField("String", "BACKEND_URL", "\"http://localhost:8081\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.compiler)

    debugImplementation(libs.bundles.compose.ui.tool)

    testImplementation(libs.junit)
    testImplementation(libs.compose.ui.test.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.squareup.okhttp3.mockwebserver)
}

kapt {
    correctErrorTypes = true
}