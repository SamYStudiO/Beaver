package net.samystudio.beaver.ui.common.dialog

/**
 * AlertDialog listener for Dialog events, only activities and fragments will received these events
 * if they implement this interface, there is no point to implement it with any other object.
 */
interface AlertDialogListener : DialogListener {
    /**
     * This is equivalent to [onDialogClick] (requestCode: Int, DialogInterface.BUTTON_POSITIVE).
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogPositive(requestCode: Int) {
    }

    /**
     * This is equivalent to [onDialogClick] (requestCode: Int, DialogInterface.BUTTON_NEGATIVE).
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogNegative(requestCode: Int) {
    }

    /**
     * This is equivalent to [onDialogClick] (requestCode: Int, DialogInterface.BUTTON_NEUTRAL).
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogNeutral(requestCode: Int) {
    }

    /**
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogClick(requestCode: Int, which: Int) {
    }

    /**
     * @see android.content.DialogInterface.OnMultiChoiceClickListener.onClick
     */
    fun onDialogClick(requestCode: Int, which: Int, checked: Boolean) {
    }
}