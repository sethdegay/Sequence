plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.hilt)
}

android {
    namespace = "dev.sethdegay.sequence.core.timer"
}

kotlin {
    compilerOptions { optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi") }
}

dependencies {
    api(projects.core.model)
    api(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
}