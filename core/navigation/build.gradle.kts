plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "dev.sethdegay.sequence.core.navigation"
}

kotlin {
    compilerOptions { optIn.add("kotlin.uuid.ExperimentalUuidApi") }
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.core)
}