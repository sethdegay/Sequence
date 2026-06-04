import org.gradle.platform.Architecture
import org.gradle.platform.OperatingSystem

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
    vendor = JvmVendorSpec.ADOPTIUM
    @Suppress("UnstableApiUsage")
    toolchainPlatforms = setOf(
        BuildPlatformFactory.of(Architecture.X86_64, OperatingSystem.LINUX),
        BuildPlatformFactory.of(Architecture.X86_64, OperatingSystem.MAC_OS),
        BuildPlatformFactory.of(Architecture.AARCH64, OperatingSystem.MAC_OS),
        BuildPlatformFactory.of(Architecture.X86_64, OperatingSystem.WINDOWS),
    )
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