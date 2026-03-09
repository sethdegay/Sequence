plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.hilt)
    alias(libs.plugins.sequence.room)
}

android {
    namespace = "dev.sethdegay.sequence.core.database"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    api(libs.kotlinx.datetime)
}