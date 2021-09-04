package net.samystudio.beaver.util

/**
 * Input as degree Celsius.
 */
fun Float.toFahrenheit() = this.toDouble().toFahrenheit().toFloat()
fun Double.toFahrenheit() = (this * 9 / 5f + 32)

fun Float.format(precision: Short = 1) = this.toDouble().format(precision)
fun Double.format(precision: Short = 1) = "%.${precision}f".format(this)

fun Int.formatDegree() = this.toDouble().formatDegree(0)
fun Long.formatDegree() = this.toDouble().formatDegree(0)
fun Float.formatDegree(precision: Short = 1) = this.toDouble().formatDegree(precision)
fun Double.formatDegree(precision: Short = 1) = "%.${precision}f°".format(this)

fun Int.formatCelsius() = this.toDouble().formatCelsius(0)
fun Long.formatCelsius() = this.toDouble().formatCelsius(0)
fun Float.formatCelsius(precision: Short = 1) = this.toDouble().formatCelsius(precision)
fun Double.formatCelsius(precision: Short = 1) = "%.${precision}f°C".format(this)

fun Int.formatFahrenheit() = this.toDouble().formatFahrenheit(0)
fun Long.formatFahrenheit() = this.toDouble().formatFahrenheit(0)
fun Float.formatFahrenheit(precision: Short = 1) = this.toDouble().formatFahrenheit(precision)
fun Double.formatFahrenheit(precision: Short = 1) = "%.${precision}f°F".format(this)
