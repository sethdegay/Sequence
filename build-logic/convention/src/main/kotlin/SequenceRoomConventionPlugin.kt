import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import dev.sethdegay.buildlogic.library
import dev.sethdegay.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class SequenceRoomConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        applyPlugins(project)
        applyRoomConfig(project)
        applyDependencies(project)
    }

    private fun applyPlugins(project: Project) {
        with(project) {
            apply(plugin = project.pluginId("ksp"))
            apply(plugin = project.pluginId("room"))
        }
    }

    private fun applyRoomConfig(project: Project) {
        with(project) {
            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }
            extensions.configure<RoomExtension> {
                schemaDirectory("${project.projectDir}/schemas")
            }
        }
    }

    private fun applyDependencies(project: Project) {
        with(project) {
            dependencies {
                "implementation"(library("androidx-room-runtime"))
                "ksp"(library("androidx-room-compiler"))
                "implementation"(library("androidx-room-ktx"))
                "testImplementation"(library("androidx-room-testing"))
            }
        }
    }
}