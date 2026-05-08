plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.segment.contextmenu.impl"
}

dependencies {
    implementation(projects.feature.segment.contextmenu.api)
}