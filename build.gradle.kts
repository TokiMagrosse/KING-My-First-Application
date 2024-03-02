buildscript {
    val agp_version by extra("8.2.2")
    val agp_version1 by extra("8.2.2")
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub_projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false

    // Add the dependency for the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}