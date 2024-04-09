plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.ring.ring.signup"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(projects.android.user.infra.network)
    implementation(projects.android.user.infra.local)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.compiler)
    debugImplementation(libs.bundles.compose.ui.tool)

    implementation(libs.androidx.appcompat)
    testImplementation(projects.android.infra.test)
    testImplementation(projects.android.user.infra.test)
    testImplementation(libs.junit)
    testImplementation(libs.compose.ui.test.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.hilt.android.testing)
}

kapt {
    correctErrorTypes = true
}