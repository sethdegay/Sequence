plugins {
    alias(libs.plugins.sequence.feature.api)
}

android {
    namespace = "dev.sethdegay.sequence.feature.sequence.editor.api"
}

kotlin {
    compilerOptions { optIn.add("kotlin.uuid.ExperimentalUuidApi") }
}