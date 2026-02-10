import dev.sethdegay.buildlogic.library
import dev.sethdegay.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class SequenceHiltConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        applyDependencies(project)
    }

    private fun applyPlugins(project: Project) {
        with(project) {
            apply(plugin = project.pluginId("ksp"))
            apply(plugin = project.pluginId("hilt"))
        }
    }

    private fun applyDependencies(project: Project) {
        with(project) {
            dependencies {
                "implementation"(library("hilt-android"))
                "ksp"(library("hilt-android-compiler"))
            }
        }
    }
}