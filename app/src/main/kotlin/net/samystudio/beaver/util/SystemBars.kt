@file:Suppress("unused")

package net.samystudio.beaver.util

import android.graphics.Color
import android.os.Build
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Helpers to handle status/navigation bars color and visibility.
 */

/**
 * A translucent color for older SDKs where light appearance were not available.
 * Should be a dark transparent color since system bar are white for these older SDKs.
 */
private val translucentSystemBarsColor = Color.parseColor("#80000000")

/**
 * @see WindowInsetsControllerCompat.isAppearanceLightStatusBars
 */
val Window.isLightStatusBars: Boolean
    get() = WindowCompat.getInsetsController(
        this,
        decorView
    ).isAppearanceLightStatusBars

/**
 * @see WindowInsetsControllerCompat.isAppearanceLightNavigationBars
 */
val Window.isLightNavigationBars: Boolean
    get() = WindowCompat.getInsetsController(
        this,
        decorView
    ).isAppearanceLightNavigationBars

/**
 * @see Window.isLightStatusBars
 */
val FragmentActivity.isLightStatusBars: Boolean
    get() = window.isLightStatusBars

/**
 * @see Window.isLightNavigationBars
 */
val FragmentActivity.isLightNavigationBars: Boolean
    get() = window.isLightNavigationBars

/**
 * @see FragmentActivity.isLightStatusBars
 */
val Fragment.isLightStatusBars: Boolean
    get() = activity?.isLightStatusBars == true

/**
 * @see FragmentActivity.isLightNavigationBars
 */
val Fragment.isLightNavigationBars: Boolean
    get() = activity?.isLightNavigationBars == true

/**
 * @see Window.isLightStatusBars
 */
val AppCompatDialogFragment.isLightStatusBars: Boolean
    get() = dialog?.window?.isLightStatusBars == true

/**
 * @see Window.isLightNavigationBars
 */
val AppCompatDialogFragment.isLightNavigationBars: Boolean
    get() = dialog?.window?.isLightNavigationBars == true

/**
 * @see WindowInsetsControllerCompat.setAppearanceLightStatusBars
 */
fun Window.toggleLightStatusBars(light: Boolean? = null) {
    WindowCompat.getInsetsController(this, decorView).let {
        val l = light ?: !it.isAppearanceLightStatusBars
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            it.isAppearanceLightStatusBars = false
            statusBarColor = if (l) translucentSystemBarsColor else Color.TRANSPARENT
        } else
            it.isAppearanceLightStatusBars = l
    }
}

/**
 * @see WindowInsetsControllerCompat.setAppearanceLightNavigationBars
 */
fun Window.toggleLightNavigationBars(light: Boolean? = null) {
    WindowCompat.getInsetsController(this, decorView).let {
        val l = light ?: !it.isAppearanceLightNavigationBars
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            it.isAppearanceLightNavigationBars = false
            navigationBarColor = if (l) translucentSystemBarsColor else Color.TRANSPARENT
        } else
            it.isAppearanceLightNavigationBars = l
    }
}

/**
 * @see WindowInsetsControllerCompat.hide
 */
fun Window.hide(@InsetsType vararg types: Int) {
    types.forEach { WindowCompat.getInsetsController(this, decorView).hide(it) }
}

fun Window.hideStatusBars() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun Window.hideNavigationBars() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun Window.hideSystemBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see WindowInsetsControllerCompat.show
 */
fun Window.show(@InsetsType vararg types: Int) {
    types.forEach { WindowCompat.getInsetsController(this, decorView).show(it) }
}

fun Window.showStatusBars() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun Window.showNavigationBars() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun Window.showSystemBars() {
    show(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see WindowInsetsControllerCompat.getSystemBarsBehavior
 * @see WindowInsetsControllerCompat.setSystemBarsBehavior
 */
var Window.systemBarsBehavior: Int
    get() = WindowCompat.getInsetsController(this, decorView).systemBarsBehavior
    set(value) {
        WindowCompat.getInsetsController(this, decorView).systemBarsBehavior = value
    }

/**
 * @see Window.toggleLightStatusBars
 */
fun FragmentActivity.toggleLightStatusBars(light: Boolean? = null) {
    window.toggleLightStatusBars(light)
}

/**
 * @see Window.toggleLightNavigationBars
 */
fun FragmentActivity.toggleLightNavigationBars(light: Boolean? = null) {
    window.toggleLightNavigationBars(light)
}

/**
 * Toggle light status/navigation bars independently.
 *
 * @see Window.toggleLightStatusBars
 * @see Window.toggleLightNavigationBars
 */
fun FragmentActivity.toggleLightSystemBars(
    lightStatus: Boolean? = null,
    lightNavigation: Boolean? = null,
) {
    window.toggleLightStatusBars(lightStatus)
    window.toggleLightNavigationBars(lightNavigation)
}

/**
 * Toggle light status/navigation bars with same value.
 *
 * @see Window.toggleLightStatusBars
 * @see Window.toggleLightNavigationBars
 */
fun FragmentActivity.toggleLightSystemBars(light: Boolean? = null) {
    window.toggleLightStatusBars(light)
    window.toggleLightNavigationBars(light)
}

/**
 * @see Window.hide
 */
fun FragmentActivity.hide(@InsetsType vararg types: Int) {
    window.hide(*types)
}

fun FragmentActivity.hideStatusBars() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun FragmentActivity.hideNavigationBars() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun FragmentActivity.hideSystemBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see Window.show
 */
fun FragmentActivity.show(@InsetsType vararg types: Int) {
    window.show(*types)
}

fun FragmentActivity.showStatusBars() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun FragmentActivity.showNavigationBars() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun FragmentActivity.showSystemBars() {
    show(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see WindowInsetsControllerCompat.getSystemBarsBehavior
 * @see WindowInsetsControllerCompat.setSystemBarsBehavior
 */
var FragmentActivity.systemBarsBehavior: Int
    get() = window.systemBarsBehavior
    set(value) {
        window.systemBarsBehavior = value
    }

/**
 * @see FragmentActivity.toggleLightStatusBars
 */
fun Fragment.toggleLightStatusBars(light: Boolean? = null) {
    activity?.toggleLightStatusBars(light)
}

/**
 * @see FragmentActivity.toggleLightNavigationBars
 */
fun Fragment.toggleLightNavigationBars(light: Boolean? = null) {
    activity?.toggleLightNavigationBars(light)
}

/**
 * Toggle light status/navigation bars independently.
 *
 * @see FragmentActivity.toggleLightStatusBars
 * @see FragmentActivity.toggleLightNavigationBars
 */
fun Fragment.toggleLightSystemBars(lightStatus: Boolean? = null, lightNavigation: Boolean? = null) {
    activity?.toggleLightSystemBars(lightStatus, lightNavigation)
}

/**
 * Toggle light status/navigation bars with same value.
 *
 * @see FragmentActivity.toggleLightStatusBars
 * @see FragmentActivity.toggleLightNavigationBars
 */
fun Fragment.toggleLightSystemBars(light: Boolean? = null) {
    activity?.toggleLightSystemBars(light)
}

/**
 * @see FragmentActivity.hide
 */
fun Fragment.hide(@InsetsType vararg types: Int) {
    activity?.hide(*types)
}

fun Fragment.hideStatusBars() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun Fragment.hideNavigationBars() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun Fragment.hideSystemBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see FragmentActivity.show
 */
fun Fragment.show(@InsetsType vararg types: Int) {
    activity?.show(*types)
}

fun Fragment.showStatusBars() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun Fragment.showNavigationBars() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun Fragment.showSystemBars() {
    show(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see WindowInsetsControllerCompat.getSystemBarsBehavior
 * @see WindowInsetsControllerCompat.setSystemBarsBehavior
 */
var Fragment.systemBarsBehavior: Int
    get() = activity?.systemBarsBehavior ?: WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    set(value) {
        activity?.systemBarsBehavior = value
    }

/**
 * @see Window.toggleLightStatusBars
 */
fun AppCompatDialogFragment.toggleLightStatusBars(light: Boolean? = null) {
    dialog?.window?.toggleLightStatusBars(light)
}

/**
 * @see Window.toggleLightNavigationBars
 */
fun AppCompatDialogFragment.toggleLightNavigationBars(light: Boolean? = null) {
    dialog?.window?.toggleLightNavigationBars(light)
}

/**
 * Toggle light status/navigation bars independently.
 *
 * @see Window.toggleLightStatusBars
 * @see Window.toggleLightNavigationBars
 */
fun AppCompatDialogFragment.toggleLightSystemBars(
    lightStatus: Boolean? = null,
    lightNavigation: Boolean? = null,
) {
    dialog?.window?.toggleLightStatusBars(lightStatus)
    dialog?.window?.toggleLightNavigationBars(lightNavigation)
}

/**
 * Toggle light status/navigation bars with same value.
 *
 * @see Window.toggleLightStatusBars
 * @see Window.toggleLightNavigationBars
 */
fun AppCompatDialogFragment.toggleLightSystemBars(light: Boolean? = null) {
    dialog?.window?.toggleLightStatusBars(light)
    dialog?.window?.toggleLightNavigationBars(light)
}

/**
 * @see Window.hide
 */
fun AppCompatDialogFragment.hide(@InsetsType vararg types: Int) {
    dialog?.window?.hide(*types)
}

fun AppCompatDialogFragment.hideStatusBars() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun AppCompatDialogFragment.hideNavigationBars() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun AppCompatDialogFragment.hideSystemBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see Window.show
 */
fun AppCompatDialogFragment.show(@InsetsType vararg types: Int) {
    dialog?.window?.show(*types)
}

fun AppCompatDialogFragment.showStatusBars() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun AppCompatDialogFragment.showNavigationBars() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun AppCompatDialogFragment.showSystemBars() {
    show(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see WindowInsetsControllerCompat.getSystemBarsBehavior
 * @see WindowInsetsControllerCompat.setSystemBarsBehavior
 */
var AppCompatDialogFragment.systemBarsBehavior: Int
    get() = dialog?.window?.systemBarsBehavior
        ?: WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    set(value) {
        dialog?.window?.systemBarsBehavior = value
    }
