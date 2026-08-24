pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Sequence"
include(":app")
include(":core:audio")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:model")
include(":core:navigation")
include(":core:timer")
include(":core:ui")
include(":feature:calendar-logs:api")
include(":feature:calendar-logs:impl")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:license:api")
include(":feature:license:impl")
include(":feature:segment-editor:api")
include(":feature:segment-editor:impl")
include(":feature:sequence-contextmenu:api")
include(":feature:sequence-contextmenu:impl")
include(":feature:sequence-editor:api")
include(":feature:sequence-editor:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
include(":feature:timer:api")
include(":feature:timer:impl")