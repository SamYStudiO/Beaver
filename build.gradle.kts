plugins {
    id("com.android.application") version Versions.android_build_tools apply false
    kotlin("android") version Versions.kotlin apply false
    kotlin("kapt") version Versions.kotlin apply false
    id("com.google.android.gms.strict-version-matcher-plugin") version Versions.strict_version_matcher_plugin apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version Versions.secret_gradle_plugin apply false
    id("com.google.gms.google-services") version Versions.google_services apply false
    id("com.google.firebase.crashlytics") version Versions.firebase_crashlytics_plugin apply false
    id("com.google.firebase.firebase-perf") version Versions.firebase_perf_plugin apply false
    id("com.google.dagger.hilt.android") version Versions.hilt apply false
    id("androidx.navigation.safeargs") version Versions.navigation apply false
    id("com.github.ben-manes.versions") version Versions.gradle_versions_plugin
    id("com.diffplug.spotless") version Versions.spotless
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
            //allWarningsAsErrors = true
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
