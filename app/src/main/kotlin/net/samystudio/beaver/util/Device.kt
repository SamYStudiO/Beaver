package net.samystudio.beaver.util

import android.os.Build

val deviceIsEmulator = (
        Build.FINGERPRINT.startsWith("google/sdk_gphone_") &&
                Build.FINGERPRINT.endsWith(":user/release-keys") &&
                Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google" &&
                Build.MODEL.startsWith("sdk_gphone_")
        ) ||
        Build.FINGERPRINT.startsWith("generic") ||
        Build.FINGERPRINT.startsWith("unknown") ||
        Build.MODEL.contains("google_sdk") ||
        Build.MODEL.contains("Emulator") ||
        Build.MODEL.contains("Android SDK built for x86") ||
        Build.MANUFACTURER.contains("Genymotion") ||
        Build.HOST.startsWith("Build") ||
        Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
        Build.PRODUCT == "google_sdk"
