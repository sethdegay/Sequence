plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.compose)
}

android {
    namespace = "dev.sethdegay.sequence.core.designsystem"
}

kotlin {
    compilerOptions { optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi") }
}