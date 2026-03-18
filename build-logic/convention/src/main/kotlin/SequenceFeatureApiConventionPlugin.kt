import dev.sethdegay.buildlogic.library
import dev.sethdegay.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class SequenceFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        applyDependencies(project)
    }

    private fun applyPlugins(project: Project) {
        with(project) {
            apply(plugin = project.pluginId("sequence-library"))
            apply(plugin = project.pluginId("kotlinx-serialization"))
        }
    }

    private fun applyDependencies(project: Project) {
        with(project) {
            dependencies {
                "api"(project(":core:navigation"))
                "implementation"(library("kotlinx-serialization-core"))
            }
        }
    }
}