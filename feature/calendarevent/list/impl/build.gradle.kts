plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.calendarevent.list.impl"
}

kotlin {
    compilerOptions { optIn.add("androidx.compose.material3.ExperimentalMaterial3Api") }
}

dependencies {
    implementation(projects.feature.calendarevent.list.api)
}