package net.samystudio.beaver.ext

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Context.getInputMethodManager(): InputMethodManager? =
    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager


fun Activity.showKeyboard(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT): Boolean {
    view.requestFocus()
    return getInputMethodManager()?.showSoftInput(view, flags) ?: false
}

fun Activity.hideKeyboard(view: View? = null, flags: Int = 0): Boolean {
    (if (view != null) view.windowToken else currentFocus?.windowToken)?.let {
        return getInputMethodManager()?.hideSoftInputFromWindow(it, flags) ?: false
    }

    return false
}

fun Activity.hideKeyboard(windowToken: IBinder, flags: Int = 0) =
    getInputMethodManager()?.hideSoftInputFromWindow(windowToken, flags) ?: false


fun Fragment.showKeyboard(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT): Boolean {
    view.requestFocus()
    return context?.getInputMethodManager()?.showSoftInput(view, flags) ?: false
}

fun Fragment.hideKeyboard(view: View? = null, flags: Int = 0): Boolean {
    (if (view != null) view.windowToken else activity?.currentFocus?.windowToken)?.let {
        return context?.getInputMethodManager()?.hideSoftInputFromWindow(it, flags) ?: false
    }

    return false
}

fun Fragment.hideKeyboard(windowToken: IBinder, flags: Int = 0) =
    context?.getInputMethodManager()?.hideSoftInputFromWindow(windowToken, flags) ?: false


fun View.showKeyboard(flags: Int = InputMethodManager.SHOW_IMPLICIT): Boolean {
    requestFocus()
    return context?.getInputMethodManager()?.showSoftInput(this, flags) ?: false
}

fun View.hideKeyboard(flags: Int = 0) =
    context?.getInputMethodManager()?.hideSoftInputFromWindow(windowToken, flags) ?: false


