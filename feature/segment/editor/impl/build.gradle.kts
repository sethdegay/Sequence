plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.segment.editor.impl"
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

dependencies {
    implementation(projects.feature.segment.editor.api)
}