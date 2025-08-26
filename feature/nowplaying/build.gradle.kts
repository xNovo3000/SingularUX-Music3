import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "org.singularux.music.feature.nowplaying"
    compileSdk = 36
    buildToolsVersion = "36.0.0"
    defaultConfig {
        minSdk = 31
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
}

dependencies {
    // Project
    api(project(":core:playback"))
    api(project(":core:ui"))
    // AndroidX
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)
    // Coil3
    implementation(libs.coil3)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    // Jetpack Compose - Preview
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)
    // Jetpack Compose - Material 3
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    // Jetpack Compose - Accompanist
    implementation(libs.compose.accompanist.permissions)
    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    // Media 3
    implementation(libs.media3.session)
}