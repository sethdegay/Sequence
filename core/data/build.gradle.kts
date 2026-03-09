plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.hilt)
}

android {
    namespace = "dev.sethdegay.sequence.core.data"
}

kotlin {
    compilerOptions { optIn.add("kotlin.uuid.ExperimentalUuidApi") }
}

dependencies {
    api(projects.core.database)
    api(projects.core.datastore)
}