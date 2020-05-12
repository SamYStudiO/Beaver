package net.samystudio.beaver.ui.common.dialog

import android.content.DialogInterface.*

/**
 * Dialog listener for Dialog events, only activities and fragments will received these events if
 * they implement this interface, there is no point to implement it with any other object.
 */
interface DialogListener {
    /**
     * @see OnShowListener.onShow
     */
    fun onDialogShow(requestCode: Int) {
    }

    /**
     * @see OnCancelListener.onCancel
     */
    fun onDialogCancel(requestCode: Int) {
    }

    /**
     * @see OnDismissListener.onDismiss
     */
    fun onDialogDismiss(requestCode: Int) {
    }
}