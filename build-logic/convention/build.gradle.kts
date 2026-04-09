import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "dev.sethdegay.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("sequenceApplication") {
            id = libs.plugins.sequence.application.get().pluginId
            implementationClass = "SequenceApplicationConventionPlugin"
        }
        register("sequenceCompose") {
            id = libs.plugins.sequence.compose.get().pluginId
            implementationClass = "SequenceComposeConventionPlugin"
        }
        register("sequenceFeatureApi") {
            id = libs.plugins.sequence.feature.api.get().pluginId
            implementationClass = "SequenceFeatureApiConventionPlugin"
        }
        register("sequenceFeatureImpl") {
            id = libs.plugins.sequence.feature.impl.get().pluginId
            implementationClass = "SequenceFeatureImplConventionPlugin"
        }
        register("sequenceHilt") {
            id = libs.plugins.sequence.hilt.get().pluginId
            implementationClass = "SequenceHiltConventionPlugin"
        }
        register("sequenceJvm") {
            id = libs.plugins.sequence.jvm.get().pluginId
            implementationClass = "SequenceJvmConventionPlugin"
        }
        register("sequenceLibrary") {
            id = libs.plugins.sequence.library.get().pluginId
            implementationClass = "SequenceLibraryConventionPlugin"
        }
        register("sequenceRoom") {
            id = libs.plugins.sequence.room.get().pluginId
            implementationClass = "SequenceRoomConventionPlugin"
        }
    }
}