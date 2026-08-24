plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.sequenceeditor.impl"
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
            "kotlinx.coroutines.FlowPreview",
            "kotlin.uuid.ExperimentalUuidApi",
        )
    }
}

dependencies {
    implementation(projects.feature.segmentEditor.api)
    implementation(projects.feature.sequenceEditor.api)
}