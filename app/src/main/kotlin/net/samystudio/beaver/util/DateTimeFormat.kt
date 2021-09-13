@file:Suppress("unused")

package net.samystudio.beaver.util

import android.text.format.DateFormat
import net.samystudio.beaver.ContextProvider.Companion.applicationContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

/**
 * Input in milliseconds
 */
fun Long.formatDuration() = this.div(1000f).roundToLong().let {
    "%d:%02d:%02d".format(
        it / 3600,
        (it % 3600) / 60,
        it % 60
    )
}

fun Long.formatShortDateTime(timeZone: TimeZone? = null) = "%s • %s".format(
    DateFormat.getDateFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this),
    DateFormat.getTimeFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this)
)

fun Long.formatShortDate(timeZone: TimeZone? = null) = "%s".format(
    DateFormat.getDateFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this)
)

fun Long.formatMediumDateTime(timeZone: TimeZone? = null) = "%s • %s".format(
    DateFormat.getMediumDateFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this),
    DateFormat.getTimeFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this)
)

fun Long.formatMediumDate(timeZone: TimeZone? = null) = "%s".format(
    DateFormat.getMediumDateFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this)
)

fun Long.formatLongDateTime(timeZone: TimeZone? = null) = "%s • %s".format(
    DateFormat.getLongDateFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this),
    DateFormat.getTimeFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this)
)

fun Long.formatLongDate(timeZone: TimeZone? = null) = "%s".format(
    DateFormat.getLongDateFormat(applicationContext).apply {
        timeZone?.let { this.timeZone = it }
    }.format(this)
)

fun Long.formatBest(pattern: String): String = SimpleDateFormat(
    DateFormat.getBestDateTimePattern(Locale.getDefault(), pattern),
    Locale.getDefault()
).format(this)
