import dev.sethdegay.buildlogic.library
import dev.sethdegay.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class SequenceFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        applyDependencies(project)
    }

    private fun applyPlugins(project: Project) {
        with(project) {
            apply(plugin = project.pluginId("sequence-library"))
            apply(plugin = project.pluginId("sequence-hilt"))
            apply(plugin = project.pluginId("sequence-compose"))
        }
    }

    private fun applyDependencies(project: Project) {
        with(project) {
            dependencies {
                "implementation"(project(":core:data"))
                "api"(project(":core:designsystem"))
                "implementation"(project(":core:ui"))

                // Coroutines
                "implementation"(library("kotlinx-coroutines-android"))
                "testImplementation"(library("kotlinx-coroutines-test"))
                "androidTestImplementation"(library("kotlinx-coroutines-test"))

                // ViewModel
                "implementation"(library("androidx-lifecycle-viewmodel-ktx"))
                "implementation"(library("androidx-lifecycle-viewmodel-compose"))

                // ViewModel/Hilt integration
                "implementation"(library("androidx-hilt-navigation-compose"))
            }
        }
    }
}