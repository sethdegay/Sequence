plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.license.impl"
}

kotlin {
    compilerOptions { optIn.add("androidx.compose.material3.ExperimentalMaterial3Api") }
}

dependencies {
    implementation(projects.feature.license.api)
    implementation(libs.aboutlibraries.compose.m3)
}