import com.android.build.api.dsl.LibraryExtension
import dev.sethdegay.buildlogic.library
import dev.sethdegay.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class SequenceComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        applyComposeConfig(project)
        applyDependencies(project)
    }

    private fun applyPlugins(project: Project) {
        with(project) {
            apply(plugin = project.pluginId("kotlin-compose"))
        }
    }

    private fun applyComposeConfig(project: Project) {
        with(project) {
            extensions.configure<LibraryExtension> {
                buildFeatures.compose = true
            }
        }
    }

    private fun applyDependencies(project: Project) {
        with(project) {
            dependencies {
                "implementation"(platform(library("androidx-compose-bom")))
                "androidTestImplementation"(platform(library("androidx-compose-bom")))

                "implementation"(library("androidx-compose-material3"))

                "implementation"(library("androidx-compose-ui-tooling-preview"))
                "debugImplementation"(library("androidx-compose-ui-tooling"))

                "androidTestImplementation"(library("androidx-compose-ui-test-junit4"))
                "debugImplementation"(library("androidx-compose-ui-test-manifest"))
            }
        }
    }
}