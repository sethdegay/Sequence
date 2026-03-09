plugins {
    alias(libs.plugins.sequence.feature)
}

android {
    namespace = "dev.sethdegay.sequence.feature.editor"
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "kotlinx.coroutines.FlowPreview",
            "kotlin.uuid.ExperimentalUuidApi",
        )
    }
}