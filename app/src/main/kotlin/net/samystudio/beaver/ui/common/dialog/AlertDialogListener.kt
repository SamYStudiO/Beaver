package net.samystudio.beaver.ui.common.dialog

import net.samystudio.beaver.ui.base.dialog.DialogListener

interface AlertDialogListener : DialogListener
{
    /**
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogPositive(requestCode: Int)
    {
    }

    /**
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogNegative(requestCode: Int)
    {
    }

    /**
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogNeutral(requestCode: Int)
    {
    }

    /**
     * @see android.content.DialogInterface.OnClickListener.onClick
     */
    fun onDialogClick(requestCode: Int, which: Int)
    {
    }

    /**
     * @see android.content.DialogInterface.OnMultiChoiceClickListener.onClick
     */
    fun onDialogClick(requestCode: Int, which: Int, checked: Boolean)
    {
    }
}