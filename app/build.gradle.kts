import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.android.gms.strict-version-matcher-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.protobuf") version Versions.protobuf_plugin
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
        versionCode = versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "FULL_VERSION_NAME", "\"$versionName build $versionCode\"")
        setProperty("archivesBaseName", "$applicationId-v$versionName($versionCode)")

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
            keyAlias = "beaver"
            keyPassword = "beaver"
            storeFile = file("keystore.jks")
            storePassword = "beaver"
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

    hilt {
        enableTransformForLocalTests = true
    }

    applicationVariants.all {
        outputs.all {
            // Since we're using date for versionCode, manifest will change each time we compile and
            // so we won't be able to use "Apply codee changes" features as it doesn't work when
            // manifest is modified. To avoid that we force a versionCode to 1 for debug build.
            val outputImpl = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
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
    network()
    debug()
    test()
    androidTest()

    implementation(Dependencies.permissionsdispatcher_ktx)
    implementation(Dependencies.insetter)
    implementation(Dependencies.flow_binding)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.protobuf}"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
