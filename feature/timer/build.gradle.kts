plugins {
    alias(libs.plugins.sequence.feature)
}

android {
    namespace = "dev.sethdegay.sequence.feature.timer"
}

kotlin {
    compilerOptions { optIn.add("androidx.compose.material3.ExperimentalMaterial3Api") }
}

dependencies {
    implementation(projects.core.timer)
}