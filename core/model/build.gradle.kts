plugins {
    alias(libs.plugins.sequence.library)
}

android {
    namespace = "dev.sethdegay.sequence.core.model"
}

kotlin {
    compilerOptions { optIn.add("kotlin.uuid.ExperimentalUuidApi") }
}