@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.common.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import androidx.annotation.*
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import androidx.appcompat.app.AlertDialog as AndroidAlertDialog

/**
 * AlertDialog using [androidx.fragment.app.DialogFragment].
 */
open class AlertDialog(
    @StringRes val titleRes: Int = 0,
    val title: CharSequence? = null,
    val customTitle: View? = null,
    @StringRes val messageRes: Int = 0,
    val message: CharSequence? = null,
    @DrawableRes val iconRes: Int = 0,
    val icon: Drawable? = null,
    @AttrRes val iconAttribute: Int = 0,
    @StringRes val positiveButtonRes: Int = 0,
    val positiveButton: CharSequence? = null,
    @DrawableRes val positiveButtonIconRes: Int = 0,
    val positiveButtonIcon: Drawable? = null,
    val positiveListener: DialogInterface.OnClickListener? = null,
    @StringRes val negativeButtonRes: Int = 0,
    val negativeButton: CharSequence? = null,
    @DrawableRes val negativeButtonIconRes: Int = 0,
    val negativeButtonIcon: Drawable? = null,
    val negativeListener: DialogInterface.OnClickListener? = null,
    @StringRes val neutralButtonRes: Int = 0,
    val neutralButton: CharSequence? = null,
    @DrawableRes val neutralButtonIconRes: Int = 0,
    val neutralButtonIcon: Drawable? = null,
    val neutralListener: DialogInterface.OnClickListener? = null,
    val cancelable: Boolean = false,
    val onCancelListener: DialogInterface.OnCancelListener? = null,
    val onDismissListener: DialogInterface.OnDismissListener? = null,
    val onKeyListener: DialogInterface.OnKeyListener? = null,
    @ArrayRes val itemsRes: Int = 0,
    val items: Array<CharSequence>? = null,
    val itemsListener: DialogInterface.OnClickListener? = null,
    val adapter: ListAdapter? = null,
    @ArrayRes val multiChoiceItemsRes: Int = 0,
    val multiChoiceItems: Array<CharSequence>? = null,
    val multiChoiceCheckedItems: BooleanArray? = null,
    val multiChoiceListener: DialogInterface.OnMultiChoiceClickListener? = null,
    @ArrayRes val singleChoiceItemsRes: Int = 0,
    val singleChoiceItems: Array<CharSequence>? = null,
    val singleChoiceAdapter: ListAdapter? = null,
    val singleChoiceCheckedItem: Int = -1,
    val singleChoiceListener: DialogInterface.OnClickListener? = null,
    val onItemSelectedListener: AdapterView.OnItemSelectedListener? = null,
    @LayoutRes val customViewRes: Int = 0,
    val customView: View? = null,
    val targetFragment: BaseFragment? = null,
    val targetRequestCode: Int? = null
) : BaseFragment(),
    DialogInterface.OnClickListener,
    DialogInterface.OnMultiChoiceClickListener {
    override val layoutViewRes: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AndroidAlertDialog.Builder(requireContext())
            .setCustomTitle(customTitle)
            .setIcon(iconRes)
            .setIcon(icon)
            .setIconAttribute(iconAttribute)
            .setCancelable(cancelable)
            .setOnCancelListener(onCancelListener)
            .setOnDismissListener(onDismissListener)
            .setOnKeyListener(onKeyListener)
            .setAdapter(adapter, this)
            .setSingleChoiceItems(
                singleChoiceAdapter,
                singleChoiceCheckedItem,
                singleChoiceListener ?: this
            )
            .setOnItemSelectedListener(onItemSelectedListener).apply {
                if (titleRes > 0)
                    setTitle(titleRes)
                else
                    setTitle(title)

                if (messageRes > 0)
                    setMessage(messageRes)
                else
                    setMessage(message)

                if (positiveButtonRes > 0)
                    setPositiveButton(positiveButtonRes, positiveListener ?: this@AlertDialog)
                else
                    setPositiveButton(positiveButton, positiveListener ?: this@AlertDialog)


                if (positiveButtonIconRes > 0)
                    setPositiveButtonIcon(requireContext().getDrawable(positiveButtonIconRes))
                else
                    setPositiveButtonIcon(positiveButtonIcon)

                if (negativeButtonRes > 0)
                    setNegativeButton(negativeButtonRes, negativeListener ?: this@AlertDialog)
                else
                    setNegativeButton(negativeButton, negativeListener ?: this@AlertDialog)

                if (negativeButtonIconRes > 0)
                    setNegativeButtonIcon(requireContext().getDrawable(negativeButtonIconRes))
                else
                    setNegativeButtonIcon(negativeButtonIcon)

                if (neutralButtonRes > 0)
                    setNeutralButton(neutralButtonRes, neutralListener ?: this@AlertDialog)
                else
                    setNeutralButton(neutralButton, neutralListener ?: this@AlertDialog)

                if (neutralButtonIconRes > 0)
                    setNeutralButtonIcon(requireContext().getDrawable(neutralButtonIconRes))
                else
                    setNeutralButtonIcon(neutralButtonIcon)

                if (itemsRes > 0)
                    setItems(itemsRes, itemsListener ?: this@AlertDialog)
                else
                    setItems(items, itemsListener ?: this@AlertDialog)

                if (multiChoiceItemsRes > 0)
                    setMultiChoiceItems(
                        multiChoiceItemsRes,
                        multiChoiceCheckedItems,
                        multiChoiceListener ?: this@AlertDialog
                    )
                else
                    setMultiChoiceItems(
                        multiChoiceItems,
                        multiChoiceCheckedItems,
                        multiChoiceListener ?: this@AlertDialog
                    )

                if (singleChoiceItemsRes > 0)
                    setSingleChoiceItems(
                        singleChoiceItemsRes,
                        singleChoiceCheckedItem,
                        singleChoiceListener ?: this@AlertDialog
                    )
                else
                    setSingleChoiceItems(
                        singleChoiceItems,
                        singleChoiceCheckedItem,
                        singleChoiceListener ?: this@AlertDialog
                    )

                if (customViewRes > 0)
                    setView(customViewRes)
                else
                    setView(customView)
            }
            .create()

    override fun onClick(dialog: DialogInterface, which: Int) {
        targetRequestCode?.let { targetRequestCode ->
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
    }

    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        targetRequestCode?.let { targetRequestCode ->
            (activity as? AlertDialogListener)?.onDialogClick(targetRequestCode, which, isChecked)

            (targetFragment as? AlertDialogListener)?.onDialogClick(
                targetRequestCode, which,
                isChecked
            )
        }
    }
}
