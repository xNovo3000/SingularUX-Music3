import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "org.singularux.music.data.playbackstate"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // Project
    api(project(":core:permission"))
    // AndroidX
    implementation(libs.androidx.core)
    // DataStore
    implementation(libs.datastore)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Room
    implementation(libs.room.android)
    ksp(libs.room.compiler)
    // Protobuf
    implementation(libs.protobuf.java)
    implementation(libs.protobuf.kotlin)
}

protobuf {
    protoc {
        // TODO: Fix this hardcoded version
        artifact = "com.google.protobuf:protoc:4.32.0"
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                id("java") {
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}