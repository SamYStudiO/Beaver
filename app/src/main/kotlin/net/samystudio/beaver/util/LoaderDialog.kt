package net.samystudio.beaver.util

import androidx.fragment.app.Fragment
import net.samystudio.beaver.ui.common.dialog.LoaderDialog

fun Fragment.showLoaderDialog() {
    /*
    We don't use navigation component here because navigating with navigation component is async and
    so if loading is really short and we immediately call hideLoaderDialog then dialog may never
    be closed.
     */
    activity?.supportFragmentManager?.let {
        if (it.findFragmentByTag(LoaderDialog::class.simpleName) == null)
            LoaderDialog().apply {
                show(it, LoaderDialog::class.simpleName)
            }
    }
}

fun Fragment.hideLoaderDialog() {
    /*
    We don't use navigation component here because navigating with navigation component is async and
    so if loading is really short and we immediately call hideLoaderDialog then dialog may never
    be closed.
    */
    (activity?.supportFragmentManager?.findFragmentByTag(LoaderDialog::class.simpleName) as? LoaderDialog)?.dismissAllowingStateLoss()
}
