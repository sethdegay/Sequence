import com.mikepenz.aboutlibraries.plugin.DuplicateMode
import com.mikepenz.aboutlibraries.plugin.DuplicateRule
import com.mikepenz.aboutlibraries.plugin.StrictMode

plugins {
    alias(libs.plugins.sequence.application)
    alias(libs.plugins.sequence.compose)
    alias(libs.plugins.sequence.hilt)
    alias(libs.plugins.aboutlibraries)
}

android {
    namespace = "dev.sethdegay.sequence"

    defaultConfig {
        applicationId = "dev.sethdegay.sequence"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)

    implementation(projects.feature.calendarevent.list.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.license.impl)
    implementation(projects.feature.segment.editor.impl)
    implementation(projects.feature.sequence.contextmenu.impl)
    implementation(projects.feature.sequence.editor.impl)
    implementation(projects.feature.settings.impl)
    implementation(projects.feature.timer.impl)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Navigation3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

aboutLibraries {
    offlineMode = false
    collect {
        fetchRemoteLicense = false
        fetchRemoteFunding = false
        filterVariants.add("release")
    }
    export {
        outputFile = file("../feature/license/impl/src/main/res/raw/licenses.json")
        prettyPrint = true
    }
    license {
        strictMode = StrictMode.FAIL
        allowedLicenses.addAll("Apache-2.0", "MIT", "BSD-3-Clause")
    }
    library {
        duplicationMode = DuplicateMode.MERGE
        duplicationRule = DuplicateRule.SIMPLE
    }
}