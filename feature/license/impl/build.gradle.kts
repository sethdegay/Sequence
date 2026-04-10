plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.license.impl"
}

dependencies {
    implementation(projects.feature.license.api)
    implementation(libs.aboutlibraries.compose.m3)
}