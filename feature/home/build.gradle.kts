import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.hilt)
}

android {
    namespace = "org.singularux.music.feature.home"
    compileSdk = 36
    buildToolsVersion = "36.0.0"
    defaultConfig {
        minSdk = 26
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
    api(project(":core:ui"))
    api(project(":data:library"))
    // AndroidX
    implementation(libs.androidx.core)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Jetpack Compose
    implementation(platform(libs.jetpack.compose.bom))
    implementation(libs.jetpack.compose.animation)
    implementation(libs.jetpack.compose.foundation)
    implementation(libs.jetpack.compose.runtime)
    implementation(libs.jetpack.compose.ui)
    // Jetpack Compose - Material 3
    implementation(libs.jetpack.compose.material3)
    // KotlinX
    implementation(libs.kotlinx.serialization.json)
}