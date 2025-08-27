@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "org.singularux.music.baseline"
    compileSdk = 36
    buildToolsVersion = "36.0.0"
    defaultConfig {
        minSdk = 31
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    targetProjectPath = ":app"
    testOptions.managedDevices.allDevices {
        create<ManagedVirtualDevice>("pixel9Api35") {
            device = "Pixel 9"
            apiLevel = 35
            systemImageSource = "aosp"
        }
    }
}

baselineProfile {
    managedDevices += "pixel9Api35"
    useConnectedDevices = false
}

dependencies {
    implementation(libs.test.espresso.core)
    implementation(libs.test.junit)
    implementation(libs.test.macrobenchmark)
    implementation(libs.test.uiautomator)
}