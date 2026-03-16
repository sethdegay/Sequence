plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.timer"
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "kotlin.uuid.ExperimentalUuidApi",
        )
    }
}

dependencies {
    implementation(projects.core.audio)
    implementation(projects.core.timer)
}