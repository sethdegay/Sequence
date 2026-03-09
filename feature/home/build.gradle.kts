plugins {
    alias(libs.plugins.sequence.feature)
}

android {
    namespace = "dev.sethdegay.sequence.feature.home"
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