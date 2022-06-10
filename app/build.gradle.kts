import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.android.gms.strict-version-matcher-plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
/*
This is a build number base on date. This solution is better than using a build number base
on version number because play console need a build number greater each time a new apk is uploaded.
When using a build number based on version if your current beta apk is 1.3.0 and you want to publish
an update to your 1.2.0 production version you won't be able to do so. We can't use build number
based on milliseconds time though because of 2100000000 version code limitation. Here we make a
build number that increment only every minute so we should never reach 2100000000.
*/
val projectStartTimeMillis = 1517443200000
val versionBuild = ((System.currentTimeMillis() - projectStartTimeMillis) / 60000).toInt()

android {
    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        applicationId = "net.samystudio.beaver"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        // We set versionCode to a fixed number to avoid breaking instant run. Right version code is
        // set later (below) for non debuggable builds.
        versionCode = 1
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.putAll(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true"
                    )
                )
            }
        }
    }

    signingConfigs {
        create("release") {
            gradleLocalProperties(rootDir).let {
                keyAlias = it.getProperty("keyAlias")
                keyPassword = it.getProperty("keyPassword")
                storeFile = file(it.getProperty("storeFile") ?: "/")
                storePassword = it.getProperty("storePassword")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    sourceSets {
        getByName("main").java.srcDir("src/main/kotlin")
        getByName("test").java.srcDir("src/test/kotlin")
        getByName("androidTest").java.srcDir("src/androidTest/kotlin")
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    applicationVariants.all {
        outputs.all {
            // We set versionCode only for release build to avoid breaking instant run
            // (Apply code changes) for debug build.
            val outputImpl = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            if (!buildType.isDebuggable)
                outputImpl.versionCodeOverride = versionBuild
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    base()
    lifecycle()
    navigation()
    room()
    work()
    firebase()
    dagger()
    reactive()
    network()
    debug()
    test()
    androidTest()

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    implementation(Dependencies.insetter)
    implementation(Dependencies.permissionlauncher)
}
