plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.hilt)
}

android {
    namespace = "dev.sethdegay.sequence.core.common"
}

dependencies {
    api(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
}