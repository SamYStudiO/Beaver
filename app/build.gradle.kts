plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
/*
This is a build number base on date. This solution is better than using a build number base
on version number because play console need a build number greater each time a new apk is uploaded.
When using a build number based on version if your current beta apk is 1.3.0 and you want to publish
an update to your 1.2.0 production version you won't be able to do so. We can't use build number
based on milliseconds time though becauseof 2100000000 version code limitation. Here we make a build
number that increment only every minute so we should never reach 2100000000.
*/
val projectStartTimeMillis = 1517443200000
val versionBuild = ((System.currentTimeMillis() - projectStartTimeMillis) / 60000).toInt()

android {
    compileSdkVersion(Versions.compileSdk)

    defaultConfig {
        applicationId = "net.samystudio.beaver".also { resValue("string", "application_id", it) }
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "FULL_VERSION_NAME", "\"$versionName.$versionCode\"")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.putAll(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "dagger.hilt.disableModulesHaveInstallInCheck" to "true"
                    )
                )
            }
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = "beaver"
            keyPassword = "beaver"
            storeFile = file("keystore.jks")
            storePassword = "beaver"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            firebaseCrashlytics {
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

    androidExtensions {
        isExperimental = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // For assisted-inject library.
    // https://kotlinlang.org/docs/reference/kapt.html#non-existent-type-correction
    kapt {
        correctErrorTypes = true
    }

    // Better output apk naming : {projectName}_{flavor(s)}_{buildType}_{versionName}_build_{buildVersion}.apk.
    applicationVariants.all {
        outputs.all {
            val outputImpl = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            val sep = "_"
            val version = versionName
            val build = versionCode
            var flavors = ""
            productFlavors.forEach { flavor ->
                flavors += "${flavor.name}${sep}"
            }
            outputImpl.outputFileName = "${rootProject.name}${sep}" +
                    flavors +
                    "${buildType.name}${sep}" +
                    "${version}${sep}" +
                    "build${sep}${build}.apk"

            // Since we're using date for versionCode, manifest will change each time we compile and
            // so we won't be able to use "Apply codee changes" features as it doesn't work when
            // manifest is modified. To avoid that we force a versionCode to 1 for debug build.
            if (name.contains("debug")) outputImpl.versionCodeOverride = 1
        }
    }
}

dependencies {
    base()
    lifecycle()
    navigation()
    room()
    firebase()
    dagger()
    reactive()
    network()
    debug()
    test()
    androidTest()

    implementation(Dependencies.permissionsdispatcher)
    kapt(Dependencies.permissionsdispatcher_processor)
}

apply(mapOf("plugin" to "com.google.gms.google-services"))
