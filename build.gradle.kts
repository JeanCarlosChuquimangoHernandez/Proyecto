// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.googleService) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false
}
buildscript {

        dependencies {
            classpath ("com.android.tools.build:gradle:8.1.4") // Versión correcta
            classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        }
}
