plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.modulegraph)
    alias(libs.plugins.room) apply false
}

tasks.named<UpdateDaemonJvm>("updateDaemonJvm") {
    languageVersion = JavaLanguageVersion.of(21)
    vendor = JvmVendorSpec.JETBRAINS
    toolchainDownloadUrls.empty()
}

moduleGraphConfig {
    graph(
        readmePath = "${rootDir}/README.md",
        heading = "#### Feature Dependency Graph"
    ) {
        showFullPath = true
        rootModulesRegex = ".*:feature:.*"
        excludedModulesRegex = ":core:.*"
    }
}