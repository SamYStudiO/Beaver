@file:SuppressLint("HardwareIds")
@file:Suppress("unused")

package net.samystudio.beaver.util

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import net.samystudio.beaver.ContextProvider
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
    get() = TimeZone.getDefault().id.let { if (it == "GMT") "Europe/Paris" else it }
val deviceTimeZoneName: String
    get() = TimeZone.getDefault().displayName

val deviceId: String? by lazy {
    Settings.Secure.getString(
        ContextProvider.applicationContext.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

val deviceScreenDensity = ContextProvider.applicationContext.resources.displayMetrics.density
val deviceScreenWidthPixels =
    ContextProvider.applicationContext.resources.displayMetrics.widthPixels
val deviceScreenHeightPixels =
    ContextProvider.applicationContext.resources.displayMetrics.heightPixels

val deviceIsEmulator =
    Build.PRODUCT.contains("sdk") ||
        Build.HARDWARE.contains("goldfish") ||
        Build.HARDWARE.contains("ranchu") ||
        deviceId == null
