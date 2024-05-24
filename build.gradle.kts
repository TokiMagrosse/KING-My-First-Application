buildscript {
    val agp_version by extra("8.2.2")
    val agp_version1 by extra("8.2.2")
    dependencies {
        classpath("com.google.firebase:perf-plugin:1.4.1") {
            exclude(group = "com.google.guava", module = "guava-jdk5")
        }
        classpath("com.google.gms:google-services:4.4.1")
    }

}
// Top-level build file where you can add configuration options common to all sub_projects/modules.
plugins {
    // To benefit from the latest Performance Monitoring plugin features,
    // update your Android Gradle plugin dependency to at least v3.4.0
    id("com.android.application") version "8.5.0-beta01" apply false

    // Make sure that you have the Google services Gradle plugin dependency
    id("com.google.gms.google-services") version "4.4.1" apply false

    // Add the dependency for the Performance Monitoring Gradle plugin
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false

    // Add the dependency for the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}