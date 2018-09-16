package net.samystudio.beaver.ext

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import net.samystudio.beaver.ui.common.dialog.AlertDialog

fun getGenericErrorDialog(context: Context): AlertDialog =
    AlertDialog.Builder(context)
        .title("Generic title")
        .message("Generic message")
        .positiveButton("Ok")
        .create()

fun DialogFragment.showIfInexistant(manager: FragmentManager, tag: String) {
    if (manager.findFragmentByTag(tag) == null)
        this.show(manager, tag)
}

fun DialogFragment.showNowIfInexistant(manager: FragmentManager, tag: String) {
    if (manager.findFragmentByTag(tag) == null)
        this.showNow(manager, tag)
}