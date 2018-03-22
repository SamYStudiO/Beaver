package net.samystudio.beaver.ext

import android.content.Context
import net.samystudio.beaver.ui.common.dialog.AlertDialog

fun getGenericErrorDialog(context: Context): AlertDialog =
    AlertDialog.Builder(context)
        .title("Generic title")
        .message("Generic message")
        .positiveButton("Ok")
        .create()