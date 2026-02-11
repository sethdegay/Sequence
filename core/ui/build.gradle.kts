plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.compose.library)
}

android {
    namespace = "dev.sethdegay.sequence.core.ui"
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
        )
    }
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.calendar.compose)
    implementation(libs.reorderable)
}