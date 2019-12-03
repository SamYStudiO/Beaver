@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.common.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.*
import kotlinx.android.parcel.Parcelize
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import androidx.appcompat.app.AlertDialog as AndroidAlertDialog

/**
 * AlertDialog using [androidx.fragment.app.DialogFragment].
 */
open class AlertDialog : BaseFragment(),
    DialogInterface.OnClickListener,
    DialogInterface.OnMultiChoiceClickListener {
    override val layoutViewRes: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: Builder? = arguments?.getParcelable(BUILDER)
        return AndroidAlertDialog.Builder(requireContext(), theme).apply {
            builder?.let {
                if (it.titleRes > 0)
                    setTitle(it.titleRes)
                else
                    setTitle(it.title)

                if (it.messageRes > 0)
                    setMessage(it.messageRes)
                else
                    setMessage(it.message)

                if (it.iconRes > 0)
                    setIcon(it.iconRes)

                if (it.iconAttribute > 0)
                    setIconAttribute(it.iconAttribute)

                if (it.positiveButtonRes > 0)
                    setPositiveButton(it.positiveButtonRes, this@AlertDialog)
                else
                    setPositiveButton(it.positiveButton, this@AlertDialog)

                if (it.positiveButtonIconRes > 0)
                    setPositiveButtonIcon(requireContext().getDrawable(it.positiveButtonIconRes))

                if (it.negativeButtonRes > 0)
                    setNegativeButton(it.negativeButtonRes, this@AlertDialog)
                else
                    setNegativeButton(it.negativeButton, this@AlertDialog)

                if (it.negativeButtonIconRes > 0)
                    setNegativeButtonIcon(requireContext().getDrawable(it.negativeButtonIconRes))

                if (it.neutralButtonRes > 0)
                    setNeutralButton(it.neutralButtonRes, this@AlertDialog)
                else
                    setNeutralButton(it.neutralButton, this@AlertDialog)

                if (it.neutralButtonIconRes > 0)
                    setNeutralButtonIcon(requireContext().getDrawable(it.neutralButtonIconRes))

                if (it.itemsRes > 0)
                    setItems(it.itemsRes, this@AlertDialog)
                else
                    setItems(it.items, this@AlertDialog)

                if (it.multiChoiceItemsRes > 0)
                    setMultiChoiceItems(
                        it.multiChoiceItemsRes,
                        it.multiChoiceCheckedItems,
                        this@AlertDialog
                    )
                else
                    setMultiChoiceItems(
                        it.multiChoiceItems,
                        it.multiChoiceCheckedItems,
                        this@AlertDialog
                    )

                if (it.singleChoiceItemsRes > 0)
                    setSingleChoiceItems(
                        it.singleChoiceItemsRes,
                        it.singleChoiceCheckedItem,
                        this@AlertDialog
                    )
                else
                    setSingleChoiceItems(
                        it.singleChoiceItems,
                        it.singleChoiceCheckedItem,
                        this@AlertDialog
                    )

                if (it.customViewRes > 0)
                    setView(it.customViewRes)
            }
        }.create()
    }


    override fun onClick(dialog: DialogInterface?, which: Int) {
        (activity as? AlertDialogListener)?.let {
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> it.onDialogPositive(targetRequestCode)
                DialogInterface.BUTTON_NEGATIVE -> it.onDialogNegative(targetRequestCode)
                DialogInterface.BUTTON_NEUTRAL -> it.onDialogNeutral(targetRequestCode)
                else -> it.onDialogClick(targetRequestCode, which)
            }
        }

        (targetFragment as? AlertDialogListener)?.let {
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> it.onDialogPositive(targetRequestCode)
                DialogInterface.BUTTON_NEGATIVE -> it.onDialogNegative(targetRequestCode)
                DialogInterface.BUTTON_NEUTRAL -> it.onDialogNeutral(targetRequestCode)
                else -> it.onDialogClick(targetRequestCode, which)
            }
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
        (activity as? AlertDialogListener)?.onDialogClick(targetRequestCode, which, isChecked)
        (targetFragment as? AlertDialogListener)?.onDialogClick(targetRequestCode, which, isChecked)
    }

    /**
     * @see [AndroidAlertDialog.Builder]
     */
    @Parcelize
    class Builder(
        @StringRes val titleRes: Int = 0,
        val title: CharSequence? = null,
        @StringRes val messageRes: Int = 0,
        val message: CharSequence? = null,
        @DrawableRes val iconRes: Int = 0,
        @AttrRes val iconAttribute: Int = 0,
        @StringRes val positiveButtonRes: Int = 0,
        val positiveButton: CharSequence? = null,
        @DrawableRes val positiveButtonIconRes: Int = 0,
        @StringRes val negativeButtonRes: Int = 0,
        val negativeButton: CharSequence? = null,
        @DrawableRes val negativeButtonIconRes: Int = 0,
        @StringRes val neutralButtonRes: Int = 0,
        val neutralButton: CharSequence? = null,
        @DrawableRes val neutralButtonIconRes: Int = 0,
        val cancelable: Boolean = false,
        @ArrayRes val itemsRes: Int = 0,
        val items: Array<CharSequence>? = null,
        @ArrayRes val multiChoiceItemsRes: Int = 0,
        val multiChoiceItems: Array<CharSequence>? = null,
        val multiChoiceCheckedItems: BooleanArray? = null,
        @ArrayRes val singleChoiceItemsRes: Int = 0,
        val singleChoiceItems: Array<CharSequence>? = null,
        val singleChoiceCheckedItem: Int = -1,
        @LayoutRes val customViewRes: Int = 0
    ) : Parcelable {

        /**
         * Create a new Alert dialog instance.
         * If you want to get result from dialog or received events, you must passed target
         * [BaseFragment] calling this dialog and that target need to implement
         * [AlertDialogListener]. Additionally if you open several dialogs you may pass
         * [targetRequestCode] to then identify which dialog your result or events come from.
         * Note you may receive events as well from activity host if activity implements
         * [AlertDialogListener].
         */
        fun build(
            targetController: BaseFragment? = null,
            targetRequestCode: Int = 0
        ): AlertDialog {
            val dialog = AlertDialog()
            dialog.setTargetFragment(targetController, targetRequestCode)
            if (dialog.arguments == null) dialog.arguments = Bundle()
            dialog.arguments?.putParcelable(BUILDER, this)
            return dialog
        }
    }

    companion object {
        private const val BUILDER = "builder"
    }
}