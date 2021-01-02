package net.samystudio.beaver.ext

import android.graphics.Color
import android.os.Build
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Helpers to handle status/navigation bars color and visibility.
 */

private val translucentBarColor = Color.parseColor("#80000000")

val Window.isLightStatusBars
    get() = WindowCompat.getInsetsController(
        this,
        decorView
    )?.isAppearanceLightStatusBars == true

val Window.isLightNavigationBars
    get() = WindowCompat.getInsetsController(
        this,
        decorView
    )?.isAppearanceLightNavigationBars == true

val FragmentActivity.isLightStatusBars
    get() = window.isLightStatusBars

val FragmentActivity.isLightNavigationBars
    get() = window.isLightNavigationBars

val Fragment.isLightStatusBars
    get() = activity?.isLightStatusBars == true

val Fragment.isLightNavigationBars
    get() = activity?.isLightNavigationBars == true

val AppCompatDialogFragment.isLightStatusBars
    get() = dialog?.window?.isLightStatusBars

val AppCompatDialogFragment.isLightNavigationBars
    get() = dialog?.window?.isLightNavigationBars

fun Window.toggleLightStatusBars(light: Boolean? = null) {
    WindowCompat.getInsetsController(this, decorView)?.let {
        val l = light ?: !it.isAppearanceLightStatusBars
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            it.isAppearanceLightStatusBars = false
            statusBarColor = if (l) translucentBarColor else Color.TRANSPARENT
        } else
            it.isAppearanceLightStatusBars = l
    }
}

fun Window.toggleLightNavigationBars(light: Boolean? = null) {
    WindowCompat.getInsetsController(this, decorView)?.let {
        val l = light ?: !it.isAppearanceLightNavigationBars
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            it.isAppearanceLightNavigationBars = false
            navigationBarColor = if (l) translucentBarColor else Color.TRANSPARENT
        } else
            it.isAppearanceLightNavigationBars = l
    }
}

/**
 * @see WindowInsetsControllerCompat.hide
 */
fun Window.hide(@InsetsType vararg types: Int) {
    types.forEach { WindowCompat.getInsetsController(this, decorView)?.hide(it) }
}

/**
 * @see WindowInsetsControllerCompat.show
 */
fun Window.show(@InsetsType vararg types: Int) {
    types.forEach { WindowCompat.getInsetsController(this, decorView)?.show(it) }
}

fun FragmentActivity.toggleLightStatusBars(light: Boolean? = null) {
    window.toggleLightStatusBars(light)
}

fun FragmentActivity.toggleLightNavigationBars(light: Boolean? = null) {
    window.toggleLightNavigationBars(light)
}

fun FragmentActivity.toggleLightBars(
    lightStatus: Boolean? = null,
    lightNavigation: Boolean? = null,
) {
    window.toggleLightStatusBars(lightStatus)
    window.toggleLightNavigationBars(lightNavigation)
}

fun FragmentActivity.toggleLightBars(light: Boolean? = null) {
    window.toggleLightStatusBars(light)
    window.toggleLightNavigationBars(light)
}

/**
 * @see WindowInsetsControllerCompat.hide
 */
fun FragmentActivity.hide(@InsetsType vararg types: Int) {
    window.hide(*types)
}

/**
 * @see WindowInsetsControllerCompat.show
 */
fun FragmentActivity.show(@InsetsType vararg types: Int) {
    window.show(*types)
}

fun Fragment.toggleLightStatusBars(light: Boolean? = null) {
    activity?.toggleLightStatusBars(light)
}

fun Fragment.toggleLightNavigationBars(light: Boolean? = null) {
    activity?.toggleLightNavigationBars(light)
}

fun Fragment.toggleLightBars(lightStatus: Boolean? = null, lightNavigation: Boolean? = null) {
    activity?.toggleLightBars(lightStatus, lightNavigation)
}

fun Fragment.toggleLightBars(light: Boolean? = null) {
    activity?.toggleLightBars(light)
}

/**
 * @see WindowInsetsControllerCompat.hide
 */
fun Fragment.hide(@InsetsType vararg types: Int) {
    activity?.hide(*types)
}

/**
 * @see WindowInsetsControllerCompat.show
 */
fun Fragment.show(@InsetsType vararg types: Int) {
    activity?.show(*types)
}

fun AppCompatDialogFragment.toggleLightStatusBars(light: Boolean? = null) {
    dialog?.window?.toggleLightStatusBars(light)
}

fun AppCompatDialogFragment.toggleLightNavigationBars(light: Boolean? = null) {
    dialog?.window?.toggleLightNavigationBars(light)
}

fun AppCompatDialogFragment.toggleLightBars(
    lightStatus: Boolean? = null,
    lightNavigation: Boolean? = null,
) {
    dialog?.window?.toggleLightStatusBars(lightStatus)
    dialog?.window?.toggleLightNavigationBars(lightNavigation)
}

fun AppCompatDialogFragment.toggleLightBars(light: Boolean? = null) {
    dialog?.window?.toggleLightStatusBars(light)
    dialog?.window?.toggleLightNavigationBars(light)
}

/**
 * @see WindowInsetsControllerCompat.hide
 */
fun AppCompatDialogFragment.hide(@InsetsType vararg types: Int) {
    dialog?.window?.hide(*types)
}

/**
 * @see WindowInsetsControllerCompat.show
 */
fun AppCompatDialogFragment.show(@InsetsType vararg types: Int) {
    dialog?.window?.show(*types)
}
