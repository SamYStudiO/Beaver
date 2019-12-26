package net.samystudio.beaver.ext

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import net.samystudio.beaver.ui.common.dialog.AlertDialog

fun getGenericErrorDialog() = AlertDialog.newInstance(
    title = "Generic title",
    message = "Generic message",
    positiveButton = "Ok"
)

fun DialogFragment.showIfNonExistent(manager: FragmentManager, tag: String) {
    if (manager.findFragmentByTag(tag) == null)
        this.show(manager, tag)
}

fun DialogFragment.showNowIfNonExistent(manager: FragmentManager, tag: String) {
    if (manager.findFragmentByTag(tag) == null)
        this.showNow(manager, tag)
}