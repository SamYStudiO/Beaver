buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.android_build_tools}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.google.gms:google-services:${Versions.google_services}")
        classpath("com.google.android.gms:strict-version-matcher-plugin:${Versions.strict_version_matcher_plugin}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.firebase_crashlytics_plugin}")
        classpath("com.google.firebase:perf-plugin:${Versions.firebase_perf_plugin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation_safe_args_plugin}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.gradle_versions_plugin}")
    }
}

plugins {
    id("com.github.ben-manes.versions") version Versions.gradle_versions_plugin
    id("com.diffplug.spotless") version Versions.spotless apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            ktlint(Versions.ktlint).userData(mapOf("disabled_rules" to "no-wildcard-imports"))
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint(Versions.ktlint)
        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
