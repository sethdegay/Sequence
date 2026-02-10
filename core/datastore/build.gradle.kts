plugins {
    alias(libs.plugins.sequence.library)
    alias(libs.plugins.sequence.hilt)
}

android {
    namespace = "dev.sethdegay.sequence.core.datastore"
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    api(libs.androidx.datastore)
    api(projects.core.datastoreProto)
    api(projects.core.model)
}