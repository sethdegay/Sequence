import com.android.build.api.dsl.ApplicationExtension
import dev.sethdegay.buildlogic.SdkVersions
import dev.sethdegay.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SequenceApplicationConventionPlugin : Plugin<Project> {
    companion object {
        private const val COMPILE_SDK = SdkVersions.APP_COMPILE_SDK
        private const val MIN_SDK = SdkVersions.APP_MIN_SDK

        private val SOURCE_COMPATIBILITY = SdkVersions.PROJECT_SOURCE_COMPATIBILITY
        private val TARGET_COMPATIBILITY = SdkVersions.PROJECT_TARGET_COMPATIBILITY
    }

    override fun apply(project: Project) {
        project.apply {
            plugin(project.pluginId("android-application"))
        }
        project.extensions.configure<ApplicationExtension> {
            compileSdk = COMPILE_SDK
            defaultConfig.apply {
                minSdk = MIN_SDK
                targetSdk = COMPILE_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            compileOptions.apply {
                sourceCompatibility = SOURCE_COMPATIBILITY
                targetCompatibility = TARGET_COMPATIBILITY
            }
        }
    }
}