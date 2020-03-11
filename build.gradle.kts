buildscript {

    repositories {
        google()
        jcenter()
        maven { url = uri("https://maven.fabric.io/public") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.android_build_tools}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.google.gms:google-services:${Versions.google_services}")
        classpath("com.google.firebase:perf-plugin:${Versions.firebase_perf_plugin}")
        classpath("io.fabric.tools:gradle:${Versions.fabric_tools}")
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation_safe_args_plugin}")
        //classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.gradle_versions_plugin}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

plugins {
    //id("com.github.ben-manes.versions")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
