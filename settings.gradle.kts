// The settings file is used to specify which projects to include in your build.

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "shogi"
include("app")
include("app:src:main:model")
findProject(":app:src:main:model")?.name = "model"
