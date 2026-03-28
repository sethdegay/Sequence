plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.sequence.compose)
    alias(libs.plugins.sequence.hilt)
}

android {
    namespace = "dev.sethdegay.sequence"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "dev.sethdegay.sequence"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.feature.calendarevent.impl)
    implementation(projects.feature.editorSegment.impl)
    implementation(projects.feature.editorSequence.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
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