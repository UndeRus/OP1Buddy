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
        flatDir {
            dir("libs")
        }
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "OP1Buddy"
include(":app")

/*
includeBuild("../libaums"){
    dependencySubstitution {
        substitute( module("me.jahnen.libaums:core")).using(project(":libaums"))
        substitute( module("me.jahnen.libaums:javafs")).using(project(":javafs"))
    }
}
*/
include(":baselineprofile")
