@file:SuppressLint("HardwareIds")
@file:Suppress("unused")

package net.samystudio.beaver.util

import android.annotation.SuppressLint
import android.os.Build
import net.samystudio.beaver.ContextProvider
import java.util.*

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

val deviceScreenDensity = ContextProvider.applicationContext.resources.displayMetrics.density
val deviceScreenWidthPixels =
    ContextProvider.applicationContext.resources.displayMetrics.widthPixels
val deviceScreenHeightPixels =
    ContextProvider.applicationContext.resources.displayMetrics.heightPixels

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
