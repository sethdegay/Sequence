plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.hilt)
}

android {
    namespace = "dev.sethdegay.sequence.core.audio"
}

dependencies {
    api(libs.kotlinx.coroutines.android)
}