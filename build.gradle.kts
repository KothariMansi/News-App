// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid)  apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.0")
    }
}