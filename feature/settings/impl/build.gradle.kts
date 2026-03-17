plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.settings.impl"
}

kotlin {
    compilerOptions { optIn.add("androidx.compose.material3.ExperimentalMaterial3Api") }
}

dependencies {
    implementation(projects.feature.settings.api)
    implementation(projects.core.audio)
}