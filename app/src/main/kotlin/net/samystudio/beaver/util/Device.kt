@file:SuppressLint("HardwareIds")
@file:Suppress("unused")

package net.samystudio.beaver.util

import android.annotation.SuppressLint
import android.os.Build
import net.samystudio.beaver.ContextProvider.Companion.applicationContext
import java.util.*

const val device = "android"
val deviceModel = Build.BRAND + "|" + Build.MODEL + "|" + Build.VERSION.RELEASE
val deviceLocale: Locale
    get() = Locale.getDefault()
val deviceLocaleLanguage: String
    get() = Locale.getDefault().language
val deviceLocaleLanguageName: String
    get() = Locale.getDefault().displayLanguage
val deviceLocaleCountry: String
    get() = Locale.getDefault().country
val deviceLocaleCountryName: String
    get() = Locale.getDefault().displayCountry
val deviceLocaleName: String
    get() = Locale.getDefault().displayName

val deviceTimeZone: TimeZone
    get() = TimeZone.getDefault()
val deviceTimeZoneId: String
    get() = TimeZone.getDefault().id
val deviceTimeZoneName: String
    get() = TimeZone.getDefault().displayName

val deviceScreenDensity = applicationContext.resources.displayMetrics.density
val deviceScreenWidthPixels =
    applicationContext.resources.displayMetrics.widthPixels
val deviceScreenHeightPixels =
    applicationContext.resources.displayMetrics.heightPixels

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
