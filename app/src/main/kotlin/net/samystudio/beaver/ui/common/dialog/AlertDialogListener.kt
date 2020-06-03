package net.samystudio.beaver.ui.common.dialog

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.DialogInterface.OnMultiChoiceClickListener

/**
 * AlertDialog listener for Dialog events, only activities and fragments will received these events
 * if they implement this interface, there is no point to implement it with any other object.
 */
interface AlertDialogListener : DialogListener {
    /**
     * This is equivalent to [onDialogClick] ([requestCode]: [Int], [DialogInterface.BUTTON_POSITIVE]).
     * @see OnClickListener.onClick
     */
    fun onDialogPositive(requestCode: Int) {
    }

    /**
     * This is equivalent to [onDialogClick] ([requestCode]: [Int], [DialogInterface.BUTTON_NEGATIVE]).
     * @see OnClickListener.onClick
     */
    fun onDialogNegative(requestCode: Int) {
    }

    /**
     * This is equivalent to [onDialogClick] ([requestCode]: [Int], [DialogInterface.BUTTON_NEUTRAL]).
     * @see OnClickListener.onClick
     */
    fun onDialogNeutral(requestCode: Int) {
    }

    /**
     * For a list returns index of clicked item.
     * This won't return [DialogInterface.BUTTON_POSITIVE], [DialogInterface.BUTTON_NEGATIVE] or
     * [DialogInterface.BUTTON_NEUTRAL], use [onDialogPositive], [onDialogNegative] or
     * [onDialogNeutral] instead.
     * @see OnClickListener.onClick
     */
    fun onDialogClick(requestCode: Int, which: Int) {
    }

    /**
     * For a list returns index checked/unchecked item.
     * This won't return [DialogInterface.BUTTON_POSITIVE], [DialogInterface.BUTTON_NEGATIVE] or
     * [DialogInterface.BUTTON_NEUTRAL], use [onDialogPositive], [onDialogNegative] or
     * [onDialogNeutral] instead.
     * @see OnMultiChoiceClickListener.onClick
     */
    fun onDialogClick(requestCode: Int, which: Int, checked: Boolean) {
    }
}