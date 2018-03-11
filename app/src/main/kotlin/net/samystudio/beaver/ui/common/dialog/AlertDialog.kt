@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.common.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.*
import android.util.TypedValue
import kotlinx.android.parcel.Parcelize
import net.samystudio.beaver.ui.base.fragment.BaseSimpleFragment
import java.util.*
import android.support.v7.app.AlertDialog as AndroidAlertDialog

/**
 * AlertDialog using [android.support.v4.app.DialogFragment].
 */
open class AlertDialog : BaseSimpleFragment(),
                         DialogInterface.OnClickListener,
                         DialogInterface.OnMultiChoiceClickListener
{
    override val layoutViewRes: Int
        get() = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): AndroidAlertDialog =
        Builder(context!!, arguments?.getParcelable(PARAMS) as? Params).build(this, theme).create()

    override fun onClick(dialog: DialogInterface?, which: Int)
    {
        activity?.let {
            if (it is AlertDialogListener)
            {
                when (which)
                {
                    DialogInterface.BUTTON_POSITIVE -> it.onDialogPositive(targetRequestCode)
                    DialogInterface.BUTTON_NEGATIVE -> it.onDialogNegative(targetRequestCode)
                    DialogInterface.BUTTON_NEUTRAL  -> it.onDialogNeutral(targetRequestCode)
                    else                            -> it.onDialogClick(targetRequestCode, which)
                }
            }
        }

        targetFragment?.let {
            if (it is AlertDialogListener)
            {
                when (which)
                {
                    DialogInterface.BUTTON_POSITIVE -> it.onDialogPositive(targetRequestCode)
                    DialogInterface.BUTTON_NEGATIVE -> it.onDialogNegative(targetRequestCode)
                    DialogInterface.BUTTON_NEUTRAL  -> it.onDialogNeutral(targetRequestCode)
                    else                            -> it.onDialogClick(targetRequestCode, which)
                }
            }
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean)
    {
        activity?.let {
            if (it is AlertDialogListener)
                it.onDialogClick(targetRequestCode, which, isChecked)
        }

        targetFragment?.let {
            if (it is AlertDialogListener)
            {
                it.onDialogClick(targetRequestCode, which, isChecked)
            }
        }
    }

    /**
     * @see [AndroidAlertDialog.Builder]
     */
    class Builder(private val context: Context)
    {
        /**
         * @hide
         */
        internal constructor(context: Context, params: Params?) : this(context)
        {
            if (params != null)
            {
                themeResId = params.themeResId
                title = params.title
                message = params.message
                titleIconId = params.titleIconId
                positiveButton = params.positiveButtonText
                negativeButton = params.negativeButtonText
                neutralButtonText = params.neutralButtonText
                items = params.items
                checkedItem = params.checkedItem
                checkedItems = params.checkedItems
                multiChoice = params.multiChoice
                singleChoice = params.singleChoice
                cancelable = params.cancelable
            }
        }

        @StyleRes
        private var themeResId: Int = 0
        private var title: CharSequence? = null
        private var message: CharSequence? = null
        @DrawableRes
        private var titleIconId: Int = 0
        private var positiveButton: CharSequence? = null
        private var negativeButton: CharSequence? = null
        private var neutralButtonText: CharSequence? = null
        private var items: Array<CharSequence>? = null
        private var checkedItem: Int = 0
        private var checkedItems: BooleanArray? = null
        private var multiChoice: Boolean = false
        private var singleChoice: Boolean = false
        private var cancelable: Boolean = true

        /**
         * If not set [AlertDialog.getTheme] will be used
         *
         * @see [AndroidAlertDialog.Builder.mTheme]
         */
        fun theme(@StyleRes themeResId: Int) = apply { this.themeResId = themeResId }

        /**
         * @see [AndroidAlertDialog.Builder.setTitle]
         */
        fun title(@StringRes titleId: Int) = title(context.getText(titleId))

        /**
         * @see [AndroidAlertDialog.Builder.setTitle]
         */
        fun title(title: CharSequence?) = apply { this.title = title }

        /**
         * @see [AndroidAlertDialog.Builder.setMessage]
         */
        fun message(@StringRes messageId: Int) = message(context.getText(messageId))

        /**
         * @see [AndroidAlertDialog.Builder.setMessage]
         */
        fun message(message: CharSequence?) = apply { this.message = message }

        /**
         * @see [AndroidAlertDialog.Builder.setIcon]
         */
        fun titleIconId(@DrawableRes titleIconId: Int) = apply { this.titleIconId = titleIconId }

        /**
         * @see [AndroidAlertDialog.Builder.setIconAttribute]
         */
        fun titleIconAttr(@AttrRes attrId: Int) = apply {
            val out = TypedValue()
            context.theme.resolveAttribute(attrId, out, true)
            titleIconId = out.resourceId
        }

        /**
         * @see [AndroidAlertDialog.Builder.setPositiveButton]
         */
        fun positiveButton(@StringRes textId: Int) = positiveButton(context.getText(textId))

        /**
         * @see [AndroidAlertDialog.Builder.setPositiveButton]
         */
        fun positiveButton(text: CharSequence) = apply { positiveButton = text }

        /**
         * @see [AndroidAlertDialog.Builder.setNegativeButton]
         */
        fun setNegativeButton(@StringRes textId: Int) = negativeButton(context.getText(textId))

        /**
         * @see [AndroidAlertDialog.Builder.setNegativeButton]
         */
        fun negativeButton(text: CharSequence) = apply { negativeButton = text }

        /**
         * @see [AndroidAlertDialog.Builder.setNeutralButton]
         */
        fun neutralButton(@StringRes textId: Int) = neutralButton(context.getText(textId))

        /**
         * @see [AndroidAlertDialog.Builder.setNeutralButton]
         */
        fun neutralButton(text: CharSequence) = apply { neutralButtonText = text }

        /**
         * @see [AndroidAlertDialog.Builder.setItems]
         */
        fun items(@ArrayRes itemsId: Int) = items(context.resources.getTextArray(itemsId))

        /**
         * @see [AndroidAlertDialog.Builder.setItems]
         */
        fun items(items: Array<CharSequence>) = apply {
            this.items = items
            singleChoice = false
            multiChoice = false
        }

        /**
         * @see [AndroidAlertDialog.Builder.setMultiChoiceItems]
         */
        fun multiChoiceItems(@ArrayRes itemsId: Int, checkedItems: BooleanArray) =
            multiChoiceItems(context.resources.getTextArray(itemsId), checkedItems)

        /**
         * @see [AndroidAlertDialog.Builder.setMultiChoiceItems]
         */
        fun multiChoiceItems(items: Array<CharSequence>, checkedItems: BooleanArray) = apply {
            this.items = items
            this.checkedItems = checkedItems
            singleChoice = false
            multiChoice = true
        }

        /**
         * @see [AndroidAlertDialog.Builder.setSingleChoiceItems]
         */
        fun singleChoiceItems(@ArrayRes itemsId: Int, checkedItem: Int) =
            singleChoiceItems(context.resources.getTextArray(itemsId), checkedItem)

        /**
         * @see [AndroidAlertDialog.Builder.setSingleChoiceItems]
         */
        fun singleChoiceItems(items: Array<CharSequence>, checkedItem: Int) = apply {
            this.items = items
            this.checkedItem = checkedItem
            singleChoice = true
            multiChoice = false
        }

        /**
         * @see [AndroidAlertDialog.Builder.setCancelable]
         */
        fun setCancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }

        fun toParams() =
            Params(themeResId,
                   title,
                   message,
                   titleIconId,
                   positiveButton,
                   negativeButton,
                   neutralButtonText,
                   items,
                   checkedItem,
                   checkedItems,
                   multiChoice,
                   singleChoice,
                   cancelable)

        fun build(dialog: AlertDialog,
                  @StyleRes themeResId: Int): AndroidAlertDialog.Builder =
            AndroidAlertDialog.Builder(context,
                                       if (this.themeResId == 0) themeResId else this.themeResId).apply {
                setTitle(title)
                setMessage(message)
                setIcon(titleIconId)
                setPositiveButton(positiveButton, dialog)
                setNegativeButton(negativeButton, dialog)
                setNegativeButton(neutralButtonText, dialog)
                if (!singleChoice && !multiChoice)
                    setItems(items, dialog)
                else if (multiChoice)
                    setMultiChoiceItems(items, checkedItems, dialog)
                else if (singleChoice)
                    setSingleChoiceItems(items, checkedItem, dialog)
                setCancelable(cancelable)
            }

        /**
         * @see newInstance
         */
        fun create(targetFragment: BaseSimpleFragment? = null,
                   targetRequestCode: Int = 0): AlertDialog
        {
            return newInstance(this, targetFragment, targetRequestCode)
        }
    }

    @Parcelize
    data class Params(@StyleRes val themeResId: Int,
                      val title: CharSequence?,
                      val message: CharSequence?,
                      val titleIconId: Int,
                      val positiveButtonText: CharSequence?,
                      val negativeButtonText: CharSequence?,
                      val neutralButtonText: CharSequence?,
                      val items: Array<CharSequence>?,
                      val checkedItem: Int,
                      val checkedItems: BooleanArray?,
                      val multiChoice: Boolean,
                      val singleChoice: Boolean,
                      val cancelable: Boolean) : Parcelable
    {
        override fun equals(other: Any?): Boolean
        {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Params

            if (themeResId != other.themeResId) return false
            if (title != other.title) return false
            if (message != other.message) return false
            if (titleIconId != other.titleIconId) return false
            if (positiveButtonText != other.positiveButtonText) return false
            if (negativeButtonText != other.negativeButtonText) return false
            if (neutralButtonText != other.neutralButtonText) return false
            if (!Arrays.equals(items, other.items)) return false
            if (checkedItem != other.checkedItem) return false
            if (!Arrays.equals(checkedItems, other.checkedItems)) return false
            if (multiChoice != other.multiChoice) return false
            if (singleChoice != other.singleChoice) return false
            if (cancelable != other.cancelable) return false

            return true
        }

        override fun hashCode(): Int
        {
            var result = themeResId
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + (message?.hashCode() ?: 0)
            result = 31 * result + titleIconId
            result = 31 * result + (positiveButtonText?.hashCode() ?: 0)
            result = 31 * result + (negativeButtonText?.hashCode() ?: 0)
            result = 31 * result + (neutralButtonText?.hashCode() ?: 0)
            result = 31 * result + (items?.let { Arrays.hashCode(it) } ?: 0)
            result = 31 * result + checkedItem
            result = 31 * result + (checkedItems?.let { Arrays.hashCode(it) } ?: 0)
            result = 31 * result + multiChoice.hashCode()
            result = 31 * result + singleChoice.hashCode()
            result = 31 * result + cancelable.hashCode()
            return result
        }
    }

    companion object
    {
        /**
         * Create a new Alert dialog instance.
         * If you want to get result from dialog or received events ([AlertDialogListener]), you
         * must passed target [BaseSimpleFragment] calling this dialog and that target need to implement
         * [AlertDialogListener]. Additionally if you open several dialogs you may pass
         * [targetRequestCode] to then identify which dialog your result or events come from.
         * Note you may receive events as well from activity host if activity implements
         * [AlertDialogListener].
         */
        fun newInstance(builder: Builder,
                        targetFragment: BaseSimpleFragment? = null,
                        targetRequestCode: Int = 0): AlertDialog
        {
            val dialog = AlertDialog()
            dialog.setTargetFragment(targetFragment, targetRequestCode)

            val bundle = Bundle()
            bundle.putParcelable(PARAMS, builder.toParams())
            dialog.arguments = bundle

            return dialog
        }

        private const val PARAMS = "params"
    }
}
