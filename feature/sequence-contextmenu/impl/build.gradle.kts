plugins {
    alias(libs.plugins.sequence.feature.impl)
}

android {
    namespace = "dev.sethdegay.sequence.feature.sequencecontextmenu.impl"
}

dependencies {
    implementation(projects.feature.sequenceContextmenu.api)
    implementation(projects.feature.sequenceEditor.api)
}