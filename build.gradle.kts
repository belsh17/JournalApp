// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) version "2.0.0" apply false
    alias(libs.plugins.kotlin.compose) apply false
}

repositories {
    google() // Google's Maven repository
    mavenCentral() // Maven Central
    // You can add other repositories here if needed, like JCenter.
}

allprojects {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central
    }
}