import dev.sethdegay.buildlogic.SdkVersions
import dev.sethdegay.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class SequenceJvmConventionPlugin : Plugin<Project> {

    companion object {
        private val SOURCE_COMPATIBILITY = SdkVersions.PROJECT_SOURCE_COMPATIBILITY
        private val TARGET_COMPATIBILITY = SdkVersions.PROJECT_TARGET_COMPATIBILITY
        private val JVM_TARGET = SdkVersions.PROJECT_JVM_TARGET
    }

    override fun apply(project: Project) {
        applyPlugins(project)
        applyJvmConfig(project)
    }

    private fun applyPlugins(project: Project) {
        project.apply {
            plugin(project.pluginId("kotlin-jvm"))
        }
    }

    private fun applyJvmConfig(project: Project) {
        project.extensions.configure<JavaPluginExtension> {
            sourceCompatibility = SOURCE_COMPATIBILITY
            targetCompatibility = TARGET_COMPATIBILITY
        }
        project.extensions.configure<KotlinJvmProjectExtension> {
            compilerOptions.jvmTarget.set(JVM_TARGET)
        }
    }
}