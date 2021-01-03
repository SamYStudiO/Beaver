package net.samystudio.beaver.ext

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

private val translucentBarColor = Color.parseColor("#80000000")

/**
 * @see WindowInsetsControllerCompat.isAppearanceLightStatusBars
 */
val Window.isLightStatusBars
    get() = WindowCompat.getInsetsController(
        this,
        decorView
    )?.isAppearanceLightStatusBars == true

/**
 * @see WindowInsetsControllerCompat.isAppearanceLightNavigationBars
 */
val Window.isLightNavigationBars
    get() = WindowCompat.getInsetsController(
        this,
        decorView
    )?.isAppearanceLightNavigationBars == true

/**
 * @see Window.isLightStatusBars
 */
val FragmentActivity.isLightStatusBars
    get() = window.isLightStatusBars

/**
 * @see Window.isLightNavigationBars
 */
val FragmentActivity.isLightNavigationBars
    get() = window.isLightNavigationBars

/**
 * @see FragmentActivity.isLightStatusBars
 */
val Fragment.isLightStatusBars
    get() = activity?.isLightStatusBars == true

/**
 * @see FragmentActivity.isLightNavigationBars
 */
val Fragment.isLightNavigationBars
    get() = activity?.isLightNavigationBars == true

/**
 * @see Window.isLightStatusBars
 */
val AppCompatDialogFragment.isLightStatusBars
    get() = dialog?.window?.isLightStatusBars

/**
 * @see Window.isLightNavigationBars
 */
val AppCompatDialogFragment.isLightNavigationBars
    get() = dialog?.window?.isLightNavigationBars

/**
 * @see WindowInsetsControllerCompat.setAppearanceLightStatusBars
 */
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

/**
 * @see WindowInsetsControllerCompat.setAppearanceLightNavigationBars
 */
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

fun Window.hideStatusBar() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun Window.hideNavigationBar() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun Window.hideBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see WindowInsetsControllerCompat.show
 */
fun Window.show(@InsetsType vararg types: Int) {
    types.forEach { WindowCompat.getInsetsController(this, decorView)?.show(it) }
}

fun Window.showStatusBar() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun Window.showNavigationBar() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun Window.showBars() {
    show(WindowInsetsCompat.Type.systemBars())
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
fun FragmentActivity.toggleLightBars(
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
fun FragmentActivity.toggleLightBars(light: Boolean? = null) {
    window.toggleLightStatusBars(light)
    window.toggleLightNavigationBars(light)
}

/**
 * @see Window.hide
 */
fun FragmentActivity.hide(@InsetsType vararg types: Int) {
    window.hide(*types)
}

fun FragmentActivity.hideStatusBar() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun FragmentActivity.hideNavigationBar() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun FragmentActivity.hideBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see Window.show
 */
fun FragmentActivity.show(@InsetsType vararg types: Int) {
    window.show(*types)
}

fun FragmentActivity.showStatusBar() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun FragmentActivity.showNavigationBar() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun FragmentActivity.showBars() {
    show(WindowInsetsCompat.Type.systemBars())
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
fun Fragment.toggleLightBars(lightStatus: Boolean? = null, lightNavigation: Boolean? = null) {
    activity?.toggleLightBars(lightStatus, lightNavigation)
}

/**
 * Toggle light status/navigation bars with same value.
 *
 * @see FragmentActivity.toggleLightStatusBars
 * @see FragmentActivity.toggleLightNavigationBars
 */
fun Fragment.toggleLightBars(light: Boolean? = null) {
    activity?.toggleLightBars(light)
}

/**
 * @see FragmentActivity.hide
 */
fun Fragment.hide(@InsetsType vararg types: Int) {
    activity?.hide(*types)
}

fun Fragment.hideStatusBar() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun Fragment.hideNavigationBar() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun Fragment.hideBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see FragmentActivity.show
 */
fun Fragment.show(@InsetsType vararg types: Int) {
    activity?.show(*types)
}

fun Fragment.showStatusBar() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun Fragment.showNavigationBar() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun Fragment.showBars() {
    show(WindowInsetsCompat.Type.systemBars())
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
fun AppCompatDialogFragment.toggleLightBars(
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
fun AppCompatDialogFragment.toggleLightBars(light: Boolean? = null) {
    dialog?.window?.toggleLightStatusBars(light)
    dialog?.window?.toggleLightNavigationBars(light)
}

/**
 * @see Window.hide
 */
fun AppCompatDialogFragment.hide(@InsetsType vararg types: Int) {
    dialog?.window?.hide(*types)
}

fun AppCompatDialogFragment.hideStatusBar() {
    hide(WindowInsetsCompat.Type.statusBars())
}

fun AppCompatDialogFragment.hideNavigationBar() {
    hide(WindowInsetsCompat.Type.navigationBars())
}

fun AppCompatDialogFragment.hideBars() {
    hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * @see Window.show
 */
fun AppCompatDialogFragment.show(@InsetsType vararg types: Int) {
    dialog?.window?.show(*types)
}

fun AppCompatDialogFragment.showStatusBar() {
    show(WindowInsetsCompat.Type.statusBars())
}

fun AppCompatDialogFragment.showNavigationBar() {
    show(WindowInsetsCompat.Type.navigationBars())
}

fun AppCompatDialogFragment.showBars() {
    show(WindowInsetsCompat.Type.systemBars())
}
