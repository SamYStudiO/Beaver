package net.samystudio.beaver.ext

import android.content.Context
import net.samystudio.beaver.ui.common.dialog.AlertDialog

fun genericErrorDialog(context: Context): AlertDialog =
    AlertDialog.Builder(context)
        .title("Error")
        .message("error")
        .positiveButton("ok")
        .create()