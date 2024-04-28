pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            // Specify the URL using setUrl method
            setUrl("https://jitpack.io")
        }
    }
}

// Set the root project name
rootProject.name = "Poisonous King"

// Include app module
include(":app")

