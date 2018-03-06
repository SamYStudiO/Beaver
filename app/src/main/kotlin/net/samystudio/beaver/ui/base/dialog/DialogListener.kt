package net.samystudio.beaver.ui.base.dialog

interface DialogListener
{
    /**
     * @see android.content.DialogInterface.OnShowListener.onShow
     */
    fun onDialogShow(requestCode: Int)
    {
    }

    /**
     * @see android.content.DialogInterface.OnCancelListener.onCancel
     */
    fun onDialogCancel(requestCode: Int)
    {
    }

    /**
     * @see android.content.DialogInterface.OnDismissListener.onDismiss
     */
    fun onDialogDismiss(requestCode: Int)
    {
    }
}