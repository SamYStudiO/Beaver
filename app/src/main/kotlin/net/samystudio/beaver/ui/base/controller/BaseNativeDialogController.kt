@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.base.controller

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

abstract class BaseNativeDialogController : BaseSimpleController(),
                                            DialogInterface.OnCancelListener,
                                            DialogInterface.OnDismissListener
{
    protected var style: Style = Style.STYLE_NORMAL
    protected var theme = 0
    protected var destroyed: Boolean = false
    protected var removed: Boolean = false
    var cancelable = true
        set(value)
        {
            field = value
            dialog?.setCancelable(value)
        }
    var showsDialog = true
    var dialog: Dialog? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)

        style = Style.values().filter { style ->
            style.id == savedInstanceState.getInt(SAVED_STYLE, Style.STYLE_NORMAL.id)
        }[0]
        theme = savedInstanceState.getInt(SAVED_THEME, 0)
        cancelable = savedInstanceState.getBoolean(SAVED_CANCELABLE, true)
        showsDialog = savedInstanceState.getBoolean(SAVED_SHOWS_DIALOG, showsDialog)
    }

    override fun onViewCreated(view: View)
    {
        super.onViewCreated(view)

        if (!showsDialog)
            return

        dialog = onCreateDialog()
        destroyed = false

        when (style)
        {
            Style.STYLE_NO_INPUT                       ->
            {
                dialog?.window?.addFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            }

            Style.STYLE_NO_FRAME, Style.STYLE_NO_TITLE -> dialog?.requestWindowFeature(
                Window.FEATURE_NO_TITLE)
            else                                       ->
            {
            }
        }

        dialog?.setContentView(view)
        dialog?.ownerActivity = activity
        dialog?.setCancelable(cancelable)
        dialog?.setOnCancelListener(this)
        dialog?.setOnDismissListener(this)
    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)

        removed = false
        dialog?.show()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        val dialogState = dialog?.onSaveInstanceState()
        dialogState?.let { outState.putBundle(SAVED_DIALOG_STATE_TAG, it) }

        if (style != Style.STYLE_NORMAL)
            outState.putInt(SAVED_STYLE, style.id)

        if (theme != 0)
            outState.putInt(SAVED_THEME, theme)

        if (!cancelable)
            outState.putBoolean(SAVED_CANCELABLE, cancelable)

        if (!showsDialog)
            outState.putBoolean(SAVED_SHOWS_DIALOG, showsDialog)
    }

    override fun onDetach(view: View)
    {
        dialog?.hide()
    }

    override fun onDestroyView(view: View)
    {
        super.onDestroyView(view)

        destroyed = true
        removed = true
        dialog?.dismiss()
        dialog = null
    }

    fun setStyle(style: Style, theme: Int)
    {
        this.style = style

        if (this.style == Style.STYLE_NO_FRAME || this.style == Style.STYLE_NO_INPUT)
            this.theme = android.R.style.Theme_Panel

        if (theme != 0)
            this.theme = theme
    }

    fun onCreateDialog(): Dialog
    {
        return Dialog(activity, theme)
    }

    fun show(router: Router, tag: String? = null)
    {
        router.pushController(RouterTransaction.with(this).tag(tag))
    }

    fun show(router: Router, transaction: RouterTransaction)
    {
        if (transaction.controller() != this)
            throw IllegalArgumentException(
                "Transaction controller must be ${javaClass.name}, found ${transaction.controller().javaClass.name}")

        router.pushController(transaction)
    }

    fun dismiss()
    {
        dialog?.dismiss()
        dialog = null
        removed = true

        finish()
    }

    override fun finish()
    {
        super.finish()

        if (showsDialog && !removed) dismiss()
    }

    override fun onCancel(dialog: DialogInterface)
    {
    }

    override fun onDismiss(dialog: DialogInterface)
    {
        if (!removed)
            dismiss()
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle)
    {
        super.onRestoreViewState(view, savedViewState)

        val dialogState = savedViewState.getBundle(SAVED_DIALOG_STATE_TAG)
        dialogState?.let { dialog?.onRestoreInstanceState(it) }
    }

    companion object
    {
        private const val SAVED_DIALOG_STATE_TAG = "BaseNativeDialogController.savedDialogState"
        private const val SAVED_STYLE = "BaseNativeDialogController.style"
        private const val SAVED_THEME = "BaseNativeDialogController.theme"
        private const val SAVED_CANCELABLE = "BaseNativeDialogController.cancelable"
        private const val SAVED_SHOWS_DIALOG = "BaseNativeDialogController.showsDialog"
    }

    enum class Style constructor(val id: Int)
    {
        STYLE_NORMAL(0), STYLE_NO_TITLE(1), STYLE_NO_FRAME(2), STYLE_NO_INPUT(3)
    }
}