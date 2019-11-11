package net.samystudio.beaver.ext

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Context.getInputMethodManager(): InputMethodManager? =
    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager


fun Activity.showKeyboard(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT) {
    view.requestFocus()
    getInputMethodManager()?.showSoftInput(view, flags)
}

fun Activity.hideKeyboard(view: View? = null, flags: Int = 0) {
    (if (view != null) view.windowToken else currentFocus?.windowToken)?.let {
        getInputMethodManager()?.hideSoftInputFromWindow(it, flags)
    }
}

fun Activity.hideKeyboard(windowToken: IBinder, flags: Int = 0) {
    getInputMethodManager()?.hideSoftInputFromWindow(windowToken, flags)
}


fun Fragment.showKeyboard(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT) {
    view.requestFocus()
    context?.getInputMethodManager()?.showSoftInput(view, flags)
}

fun Fragment.hideKeyboard(view: View? = null, flags: Int = 0) {
    (if (view != null) view.windowToken else activity?.currentFocus?.windowToken)?.let {
        context?.getInputMethodManager()?.hideSoftInputFromWindow(it, flags)
    }
}

fun Fragment.hideKeyboard(windowToken: IBinder, flags: Int = 0) {
    context?.getInputMethodManager()?.hideSoftInputFromWindow(windowToken, flags)
}


fun View.showKeyboard(flags: Int = InputMethodManager.SHOW_IMPLICIT) {
    requestFocus()
    context?.getInputMethodManager()?.showSoftInput(this, flags)
}

fun View.hideKeyboard(flags: Int = 0) {
    context?.getInputMethodManager()?.hideSoftInputFromWindow(windowToken, flags)
}

