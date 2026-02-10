package dev.sethdegay.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

private fun VersionCatalog.findPluginOrThrow(alias: String): Provider<PluginDependency> {
    return findPlugin(alias).orElseThrow {
        NoSuchElementException("Plugin alias '$alias' not found in version catalog.")
    }
}

private fun VersionCatalog.findLibraryOrThrow(alias: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(alias).orElseThrow {
        NoSuchElementException("Library alias '$alias' not found in version catalog.")
    }
}

fun Project.pluginId(alias: String): String =
    libs.findPluginOrThrow(alias).get().pluginId

fun Project.library(alias: String): Provider<MinimalExternalModuleDependency> =
    libs.findLibraryOrThrow(alias)