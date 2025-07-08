import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
}

android {
    namespace = "org.singularux.music"
    compileSdk = 36
    buildToolsVersion = "36.0.0"
    defaultConfig {
        applicationId = "org.singularux.music"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "20250705"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    api(project(":feature:playback"))
    api(project(":feature:home"))
    // AndroidX
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.navigation3)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Jetpack Compose
    implementation(platform(libs.jetpack.compose.bom))
    implementation(libs.jetpack.compose.animation)
    implementation(libs.jetpack.compose.foundation)
    implementation(libs.jetpack.compose.runtime)
    implementation(libs.jetpack.compose.ui)
    // Navigation 3
    implementation(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
}