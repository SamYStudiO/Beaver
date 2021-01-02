@file:SuppressLint("HardwareIds")

package net.samystudio.beaver.ext

import android.annotation.SuppressLint
import android.os.Build
import java.util.*

val deviceLanguage: String
    get() = Locale.getDefault().language
val deviceLocale: String
    get() = Locale.getDefault().displayName
val deviceTimeZone: String
    get() = TimeZone.getDefault().id
val deviceIsEmulator
    get() = Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || Build.PRODUCT == "sdk_gphone_x86"
