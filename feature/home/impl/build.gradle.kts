plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.home.impl"
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
            "kotlinx.coroutines.ExperimentalCoroutinesApi",
            "kotlin.uuid.ExperimentalUuidApi",
        )
    }
}

dependencies {
    implementation(projects.feature.calendarevent.api)
    implementation(projects.feature.editorSequence.api)
    implementation(projects.feature.home.api)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.timer.api)
}