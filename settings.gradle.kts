pluginManagement {
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

rootProject.name = "Dragon Ball"
include(":app")
include(":core:network")
include(":core:database")
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:datastore")
include(":core:navigation")
include(":core:designsystem")
include(":feature:characters")
include(":feature:character-details")
