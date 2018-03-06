package net.samystudio.beaver.ui.base.dialog

interface DialogListener
{
    /**
     * @see android.content.DialogInterface.OnCancelListener.onCancel
     */
    fun onCancelDialog(requestCode: Int)
    {
    }

    /**
     * @see android.content.DialogInterface.OnDismissListener.onDismiss
     */
    fun onDismissDialog(requestCode: Int)
    {
    }
}