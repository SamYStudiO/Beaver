@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.common.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import androidx.appcompat.app.AlertDialog as AndroidAlertDialog

/**
 * AlertDialog using [DialogFragment].
 */
open class AlertDialog : BaseFragment(),
    DialogInterface.OnClickListener,
    DialogInterface.OnMultiChoiceClickListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AndroidAlertDialog.Builder(requireContext(), theme).apply {
            arguments?.apply {
                getInt(KEY_TITLE_RES).let {
                    if (it != 0)
                        setTitle(it)
                    else
                        setTitle(getCharSequence(KEY_TITLE))
                }
                getInt(KEY_MESSAGE_RES).let {
                    if (it != 0)
                        setMessage(it)
                    else
                        setMessage(getCharSequence(KEY_MESSAGE))
                }
                getInt(KEY_ICON_RES).let { if (it != 0) setIcon(it) }
                getInt(KEY_ICON_ATTRIBUTE).let { if (it != 0) setIconAttribute(it) }
                getInt(KEY_POSITIVE_BUTTON_RES).let {
                    if (it != 0)
                        setPositiveButton(it, this@AlertDialog)
                    else
                        getCharSequence(KEY_POSITIVE_BUTTON)?.let { text ->
                            setPositiveButton(text, this@AlertDialog)
                        }
                }
                getInt(KEY_POSITIVE_BUTTON_ICON_RES).let {
                    if (it != 0) setPositiveButtonIcon(
                        context.getDrawable(it)
                    )
                }
                getInt(KEY_NEGATIVE_BUTTON_RES).let {
                    if (it != 0)
                        setNegativeButton(it, this@AlertDialog)
                    else
                        getCharSequence(KEY_NEGATIVE_BUTTON)?.let { text ->
                            setNegativeButton(text, this@AlertDialog)
                        }
                }
                getInt(KEY_NEGATIVE_BUTTON_ICON_RES).let {
                    if (it != 0) setNegativeButtonIcon(
                        context.getDrawable(it)
                    )
                }
                getInt(KEY_NEUTRAL_BUTTON_RES).let {
                    if (it != 0)
                        setNeutralButton(it, this@AlertDialog)
                    else
                        getCharSequence(KEY_NEUTRAL_BUTTON)?.let { text ->
                            setNeutralButton(text, this@AlertDialog)
                        }
                }
                getInt(KEY_NEUTRAL_BUTTON_ICON_RES).let {
                    if (it != 0) setNegativeButtonIcon(
                        context.getDrawable(it)
                    )
                }
                setCancelable(getBoolean(KEY_CANCELABLE, true))
                getInt(KEY_ITEMS_RES).let {
                    if (it != 0)
                        setItems(it, this@AlertDialog)
                    else
                        getCharSequenceArray(KEY_ITEMS)?.let { items ->
                            setItems(items, this@AlertDialog)
                        }
                }
                getInt(KEY_MULTI_CHOICE_ITEMS_RES).let {
                    if (it != 0)
                        setMultiChoiceItems(
                            it,
                            getBooleanArray(KEY_MULTI_CHOICE_CHECKED_ITEMS),
                            this@AlertDialog
                        )
                    else
                        getCharSequenceArray(KEY_MULTI_CHOICE_ITEMS)?.let { items ->
                            setMultiChoiceItems(
                                items,
                                getBooleanArray(KEY_MULTI_CHOICE_CHECKED_ITEMS),
                                this@AlertDialog
                            )
                        }
                }
                getInt(KEY_SINGLE_CHOICE_ITEMS_RES).let {
                    if (it != 0)
                        setSingleChoiceItems(
                            it,
                            getInt(KEY_SINGLE_CHOICE_CHECKED_ITEM),
                            this@AlertDialog
                        )
                    else
                        getCharSequenceArray(KEY_SINGLE_CHOICE_ITEMS)?.let { items ->
                            setSingleChoiceItems(
                                items, getInt(KEY_SINGLE_CHOICE_CHECKED_ITEM), this@AlertDialog
                            )
                        }
                }
                getInt(KEY_CUSTOM_VIEW_RES).let { if (it != 0) setView(it) }
            }
            onPrepareDialogBuilder(this)
        }.create()
    }

    override fun onShow(dialog: DialogInterface) {
        super.onShow(dialog)

        setButtonColor(DialogInterface.BUTTON_POSITIVE)
        setButtonColor(DialogInterface.BUTTON_NEGATIVE)
        setButtonColor(DialogInterface.BUTTON_NEUTRAL)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
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

        // This is a single choice callback, if we have not set a positive button let's assume we
        // want to dismiss dialog when an item is selected.
        if (arguments?.getInt(KEY_POSITIVE_BUTTON_RES) == 0
            && arguments?.getCharSequence(KEY_POSITIVE_BUTTON).isNullOrBlank()
        ) dismissAllowingStateLoss()
    }

    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        (activity as? AlertDialogListener)?.let {
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> it.onDialogPositive(targetRequestCode)
                DialogInterface.BUTTON_NEGATIVE -> it.onDialogNegative(targetRequestCode)
                DialogInterface.BUTTON_NEUTRAL -> it.onDialogNeutral(targetRequestCode)
                else -> it.onDialogClick(targetRequestCode, which, isChecked)
            }
        }

        (targetFragment as? AlertDialogListener)?.let {
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> it.onDialogPositive(targetRequestCode)
                DialogInterface.BUTTON_NEGATIVE -> it.onDialogNegative(targetRequestCode)
                DialogInterface.BUTTON_NEUTRAL -> it.onDialogNeutral(targetRequestCode)
                else -> it.onDialogClick(targetRequestCode, which, isChecked)
            }
        }
    }

    protected open fun onPrepareDialogBuilder(builder: AndroidAlertDialog.Builder) {}

    private fun setButtonColor(whichButton: Int) {
        val keyButtonColorStateListRes = when (whichButton) {
            DialogInterface.BUTTON_POSITIVE -> KEY_POSITIVE_BUTTON_COLOR_STATE_LIST_RES
            DialogInterface.BUTTON_NEGATIVE -> KEY_NEGATIVE_BUTTON_COLOR_STATE_LIST_RES
            DialogInterface.BUTTON_NEUTRAL -> KEY_NEUTRAL_BUTTON_COLOR_STATE_LIST_RES
            else -> ""
        }

        val keyButtonColorStateList = when (whichButton) {
            DialogInterface.BUTTON_POSITIVE -> KEY_POSITIVE_BUTTON_COLOR_STATE_LIST
            DialogInterface.BUTTON_NEGATIVE -> KEY_NEGATIVE_BUTTON_COLOR_STATE_LIST
            DialogInterface.BUTTON_NEUTRAL -> KEY_NEUTRAL_BUTTON_COLOR_STATE_LIST
            else -> ""
        }

        val keyButtonColorRes = when (whichButton) {
            DialogInterface.BUTTON_POSITIVE -> KEY_POSITIVE_BUTTON_COLOR_RES
            DialogInterface.BUTTON_NEGATIVE -> KEY_NEGATIVE_BUTTON_COLOR_RES
            DialogInterface.BUTTON_NEUTRAL -> KEY_NEUTRAL_BUTTON_COLOR_RES
            else -> ""
        }

        val keyButtonColor = when (whichButton) {
            DialogInterface.BUTTON_POSITIVE -> KEY_POSITIVE_BUTTON_COLOR
            DialogInterface.BUTTON_NEGATIVE -> KEY_NEGATIVE_BUTTON_COLOR
            DialogInterface.BUTTON_NEUTRAL -> KEY_NEUTRAL_BUTTON_COLOR
            else -> ""
        }

        (dialog as? AndroidAlertDialog)?.getButton(whichButton)?.let { button ->
            arguments?.getInt(keyButtonColorStateListRes)?.let {
                if (it != 0) {
                    ContextCompat.getColorStateList(requireContext(), it)
                        .let { colorStateList -> button.setTextColor(colorStateList) }
                } else {
                    arguments?.getParcelable<ColorStateList>(keyButtonColorStateList)
                        ?.let { colorStateList -> button.setTextColor(colorStateList) } ?: run {
                        arguments?.getInt(keyButtonColorRes)?.let { color ->
                            if (color != 0)
                                button.setTextColor(ContextCompat.getColor(requireContext(), color))
                            else if (arguments?.containsKey(keyButtonColor) == true)
                                button.setTextColor(
                                    arguments?.getInt(keyButtonColor) ?: 0
                                )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val KEY_TITLE_RES = "titleRes"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE_RES = "messageRes"
        const val KEY_MESSAGE = "message"
        const val KEY_ICON_RES = "iconRes"
        const val KEY_ICON_ATTRIBUTE = "iconAttribute"
        const val KEY_POSITIVE_BUTTON_RES = "positiveButtonRes"
        const val KEY_POSITIVE_BUTTON = "positiveButton"
        const val KEY_POSITIVE_BUTTON_ICON_RES = "positiveButtonIconRes"
        const val KEY_POSITIVE_BUTTON_COLOR = "positiveButtonColor"
        const val KEY_POSITIVE_BUTTON_COLOR_RES = "positiveButtonColorRes"
        const val KEY_POSITIVE_BUTTON_COLOR_STATE_LIST = "positiveButtonColorStateList"
        const val KEY_POSITIVE_BUTTON_COLOR_STATE_LIST_RES = "positiveButtonColorStateListRes"
        const val KEY_NEGATIVE_BUTTON_RES = "negativeButtonRes"
        const val KEY_NEGATIVE_BUTTON = "negativeButton"
        const val KEY_NEGATIVE_BUTTON_ICON_RES = "negativeButtonIconRes"
        const val KEY_NEGATIVE_BUTTON_COLOR = "negativeButtonColor"
        const val KEY_NEGATIVE_BUTTON_COLOR_RES = "negativeButtonColorRes"
        const val KEY_NEGATIVE_BUTTON_COLOR_STATE_LIST = "negativeButtonColorStateList"
        const val KEY_NEGATIVE_BUTTON_COLOR_STATE_LIST_RES = "negativeButtonColorStateListRes"
        const val KEY_NEUTRAL_BUTTON_RES = "neutralButtonRes"
        const val KEY_NEUTRAL_BUTTON = "neutralButton"
        const val KEY_NEUTRAL_BUTTON_ICON_RES = "neutralButtonIconRes"
        const val KEY_NEUTRAL_BUTTON_COLOR = "neutralButtonColor"
        const val KEY_NEUTRAL_BUTTON_COLOR_RES = "neutralButtonColorRes"
        const val KEY_NEUTRAL_BUTTON_COLOR_STATE_LIST = "neutralButtonColorStateList"
        const val KEY_NEUTRAL_BUTTON_COLOR_STATE_LIST_RES = "neutralButtonColorStateListRes"
        const val KEY_CANCELABLE = "cancelable"
        const val KEY_ITEMS_RES = "itemsRes"
        const val KEY_ITEMS = "items"
        const val KEY_MULTI_CHOICE_ITEMS_RES = "multiChoiceItemsRes"
        const val KEY_MULTI_CHOICE_ITEMS = "multiChoiceItems"
        const val KEY_MULTI_CHOICE_CHECKED_ITEMS = "multiChoiceCheckedItems"
        const val KEY_SINGLE_CHOICE_ITEMS_RES = "singleChoiceItemsRes"
        const val KEY_SINGLE_CHOICE_ITEMS = "singleChoiceItems"
        const val KEY_SINGLE_CHOICE_CHECKED_ITEM = "singleChoiceCheckedItem"
        const val KEY_CUSTOM_VIEW_RES = "customViewRes"

        /**
         * Create a new Alert dialog instance.
         * If you want to get result from dialog or received events ([AlertDialogListener]), you
         * must passed target [Fragment] calling this dialog and that target need to
         * implement [AlertDialogListener]. Additionally if you open several dialogs you may pass
         * [targetRequestCode] to then identify which dialog your result or events come from.
         * Note you may receive events as well from activity host if activity implements
         * [AlertDialogListener].
         */
        fun newInstance(
            @StringRes titleRes: Int = 0,
            title: CharSequence? = null,
            @StringRes messageRes: Int = 0,
            message: CharSequence? = null,
            @DrawableRes iconRes: Int = 0,
            @AttrRes iconAttribute: Int = 0,
            @StringRes positiveButtonRes: Int = 0,
            positiveButton: CharSequence? = null,
            @DrawableRes positiveButtonIconRes: Int = 0,
            @ColorInt positiveButtonColor: Int? = null,
            @ColorRes positiveButtonColorRes: Int = 0,
            positiveButtonColorStateList: ColorStateList? = null,
            @ColorRes positiveButtonColorStateListRes: Int = 0,
            @StringRes negativeButtonRes: Int = 0,
            negativeButton: CharSequence? = null,
            @DrawableRes negativeButtonIconRes: Int = 0,
            @ColorInt negativeButtonColor: Int? = null,
            @ColorRes negativeButtonColorRes: Int = 0,
            negativeButtonColorStateList: ColorStateList? = null,
            @ColorRes negativeButtonColorStateListRes: Int = 0,
            @StringRes neutralButtonRes: Int = 0,
            neutralButton: CharSequence? = null,
            @DrawableRes neutralButtonIconRes: Int = 0,
            @ColorInt neutralButtonColor: Int? = null,
            @ColorRes neutralButtonColorRes: Int = 0,
            neutralButtonColorStateList: ColorStateList? = null,
            @ColorRes neutralButtonColorStateListRes: Int = 0,
            cancelable: Boolean = true,
            @ArrayRes itemsRes: Int = 0,
            items: Array<CharSequence>? = null,
            @ArrayRes multiChoiceItemsRes: Int = 0,
            multiChoiceItems: Array<CharSequence>? = null,
            multiChoiceCheckedItems: BooleanArray? = null,
            @ArrayRes singleChoiceItemsRes: Int = 0,
            singleChoiceItems: Array<CharSequence>? = null,
            singleChoiceCheckedItem: Int = -1,
            @LayoutRes customViewRes: Int = 0,
            targetFragment: Fragment? = null,
            targetRequestCode: Int = 0
        ) = AlertDialog().apply {
            setTargetFragment(targetFragment, targetRequestCode)
            arguments = buildArguments(
                titleRes,
                title,
                messageRes,
                message,
                iconRes,
                iconAttribute,
                positiveButtonRes,
                positiveButton,
                positiveButtonIconRes,
                positiveButtonColor,
                positiveButtonColorRes,
                positiveButtonColorStateList,
                positiveButtonColorStateListRes,
                negativeButtonRes,
                negativeButton,
                negativeButtonIconRes,
                negativeButtonColor,
                negativeButtonColorRes,
                negativeButtonColorStateList,
                negativeButtonColorStateListRes,
                neutralButtonRes,
                neutralButton,
                neutralButtonIconRes,
                neutralButtonColor,
                neutralButtonColorRes,
                neutralButtonColorStateList,
                neutralButtonColorStateListRes,
                cancelable,
                itemsRes,
                items,
                multiChoiceItemsRes,
                multiChoiceItems,
                multiChoiceCheckedItems,
                singleChoiceItemsRes,
                singleChoiceItems,
                singleChoiceCheckedItem,
                customViewRes
            )
        }

        @JvmStatic
        protected fun buildArguments(
            @StringRes titleRes: Int = 0,
            title: CharSequence? = null,
            @StringRes messageRes: Int = 0,
            message: CharSequence? = null,
            @DrawableRes iconRes: Int = 0,
            @AttrRes iconAttribute: Int = 0,
            @StringRes positiveButtonRes: Int = 0,
            positiveButton: CharSequence? = null,
            @DrawableRes positiveButtonIconRes: Int = 0,
            @ColorInt positiveButtonColor: Int? = null,
            @ColorRes positiveButtonColorRes: Int = 0,
            positiveButtonColorStateList: ColorStateList? = null,
            @ColorRes positiveButtonColorStateListRes: Int = 0,
            @StringRes negativeButtonRes: Int = 0,
            negativeButton: CharSequence? = null,
            @DrawableRes negativeButtonIconRes: Int = 0,
            @ColorInt negativeButtonColor: Int? = null,
            @ColorRes negativeButtonColorRes: Int = 0,
            negativeButtonColorStateList: ColorStateList? = null,
            @ColorRes negativeButtonColorStateListRes: Int = 0,
            @StringRes neutralButtonRes: Int = 0,
            neutralButton: CharSequence? = null,
            @DrawableRes neutralButtonIconRes: Int = 0,
            @ColorInt neutralButtonColor: Int? = null,
            @ColorRes neutralButtonColorRes: Int = 0,
            neutralButtonColorStateList: ColorStateList? = null,
            @ColorRes neutralButtonColorStateListRes: Int = 0,
            cancelable: Boolean = true,
            @ArrayRes itemsRes: Int = 0,
            items: Array<CharSequence>? = null,
            @ArrayRes multiChoiceItemsRes: Int = 0,
            multiChoiceItems: Array<CharSequence>? = null,
            multiChoiceCheckedItems: BooleanArray? = null,
            @ArrayRes singleChoiceItemsRes: Int = 0,
            singleChoiceItems: Array<CharSequence>? = null,
            singleChoiceCheckedItem: Int = -1,
            @LayoutRes customViewRes: Int = 0
        ) = Bundle().apply {
            if (titleRes != 0) putInt(KEY_TITLE_RES, titleRes)
            putCharSequence(KEY_TITLE, title)
            if (messageRes != 0) putInt(KEY_MESSAGE_RES, messageRes)
            putCharSequence(KEY_MESSAGE, message)
            putInt(KEY_ICON_RES, iconRes)
            putInt(KEY_ICON_ATTRIBUTE, iconAttribute)
            if (positiveButtonRes != 0) putInt(KEY_POSITIVE_BUTTON_RES, positiveButtonRes)
            putCharSequence(KEY_POSITIVE_BUTTON, positiveButton)
            putInt(KEY_POSITIVE_BUTTON_ICON_RES, positiveButtonIconRes)
            positiveButtonColor?.let { putInt(KEY_POSITIVE_BUTTON_COLOR, it) }
            if (positiveButtonRes != 0) putInt(
                KEY_POSITIVE_BUTTON_COLOR_RES,
                positiveButtonColorRes
            )
            putParcelable(KEY_POSITIVE_BUTTON_COLOR_STATE_LIST, positiveButtonColorStateList)
            if (positiveButtonColorStateListRes != 0) putInt(
                KEY_POSITIVE_BUTTON_COLOR_STATE_LIST_RES,
                positiveButtonColorStateListRes
            )
            if (negativeButtonRes != 0) putInt(KEY_NEGATIVE_BUTTON_RES, negativeButtonRes)
            putCharSequence(KEY_NEGATIVE_BUTTON, negativeButton)
            putInt(KEY_NEGATIVE_BUTTON_ICON_RES, negativeButtonIconRes)
            negativeButtonColor?.let { putInt(KEY_NEGATIVE_BUTTON_COLOR, it) }
            if (negativeButtonRes != 0) putInt(
                KEY_NEGATIVE_BUTTON_COLOR_RES,
                negativeButtonColorRes
            )
            putParcelable(KEY_NEGATIVE_BUTTON_COLOR_STATE_LIST, negativeButtonColorStateList)
            if (negativeButtonColorStateListRes != 0) putInt(
                KEY_NEGATIVE_BUTTON_COLOR_STATE_LIST_RES,
                negativeButtonColorStateListRes
            )
            if (neutralButtonRes != 0) putInt(KEY_NEUTRAL_BUTTON_RES, neutralButtonRes)
            putCharSequence(KEY_NEUTRAL_BUTTON, neutralButton)
            putInt(KEY_NEUTRAL_BUTTON_ICON_RES, neutralButtonIconRes)
            neutralButtonColor?.let { putInt(KEY_NEUTRAL_BUTTON_COLOR, it) }
            if (neutralButtonRes != 0) putInt(KEY_NEUTRAL_BUTTON_COLOR_RES, neutralButtonColorRes)
            putParcelable(KEY_NEUTRAL_BUTTON_COLOR_STATE_LIST, neutralButtonColorStateList)
            if (neutralButtonColorStateListRes != 0) putInt(
                KEY_NEUTRAL_BUTTON_COLOR_STATE_LIST_RES,
                neutralButtonColorStateListRes
            )
            putBoolean(KEY_CANCELABLE, cancelable)
            if (itemsRes != 0) putInt(KEY_ITEMS_RES, itemsRes)
            putCharSequenceArray(KEY_ITEMS, items)
            if (multiChoiceItemsRes != 0) putInt(
                KEY_MULTI_CHOICE_ITEMS_RES,
                multiChoiceItemsRes
            )
            putCharSequenceArray(KEY_MULTI_CHOICE_ITEMS, multiChoiceItems)
            putBooleanArray(KEY_MULTI_CHOICE_CHECKED_ITEMS, multiChoiceCheckedItems)
            if (singleChoiceItemsRes != 0) putInt(
                KEY_SINGLE_CHOICE_ITEMS_RES,
                singleChoiceItemsRes
            )
            putCharSequenceArray(KEY_SINGLE_CHOICE_ITEMS, singleChoiceItems)
            putInt(KEY_SINGLE_CHOICE_CHECKED_ITEM, singleChoiceCheckedItem)
            putInt(KEY_CUSTOM_VIEW_RES, customViewRes)
        }
    }
}
