package net.samystudio.beaver.ui.common.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.app.AlertDialog as AndroidAlertDialog

/**
 * AlertDialog using [DialogFragment].
 */
open class AlertDialog :
    AppCompatDialogFragment(),
    DialogInterface.OnClickListener,
    DialogInterface.OnMultiChoiceClickListener,
    DialogInterface.OnShowListener {
    private val extras
        get() = arguments?.getBundle(KEY_EXTRAS) ?: bundleOf()
    private val requestCode
        get() = arguments?.getInt(KEY_REQUEST_CODE) ?: 0
    private val requestCodePair
        get() = KEY_REQUEST_CODE to requestCode

    /**
     * Store current selected index for single choice dialogs.
     */
    private var selectedIndex: Int? = null

    /**
     * Store current selected indices for multiple choice dialogs.
     */
    private var selectedIndices: MutableSet<Int>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme).apply {
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
                getInt(KEY_ICON_ATTRIBUTE).let { iconAttribute ->
                    if (iconAttribute != 0) {
                        setIcon(
                            handleIcon(
                                TypedValue().apply {
                                    requireContext().theme.resolveAttribute(
                                        iconAttribute,
                                        this,
                                        true
                                    )
                                }.resourceId
                            )
                        )
                    } else {
                        getInt(KEY_ICON_RES).let { iconRes ->
                            if (iconRes != 0) setIcon(handleIcon(iconRes))
                            else setIcon(
                                getParcelable<Bitmap>(KEY_ICON_BITMAP)?.toDrawable(
                                    resources
                                )
                            )
                        }
                    }
                }

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
                        ContextCompat.getDrawable(context, it)
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
                        ContextCompat.getDrawable(context, it)
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
                        ContextCompat.getDrawable(context, it)
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
                    val indices = mutableSetOf<Int>()
                    getBooleanArray(KEY_MULTI_CHOICE_CHECKED_ITEMS).apply {
                        this?.mapIndexed { index, b -> index to b }
                            ?.filter { pair -> pair.second }
                            ?.map { pair -> pair.first }
                            ?.forEach { index -> indices.add(index) }
                    }
                    if (it != 0) {
                        selectedIndices = indices
                        setMultiChoiceItems(
                            it,
                            getBooleanArray(KEY_MULTI_CHOICE_CHECKED_ITEMS),
                            this@AlertDialog
                        )
                    } else
                        getCharSequenceArray(KEY_MULTI_CHOICE_ITEMS)?.let { items ->
                            selectedIndices = indices
                            setMultiChoiceItems(
                                items,
                                getBooleanArray(KEY_MULTI_CHOICE_CHECKED_ITEMS),
                                this@AlertDialog
                            )
                        }
                }
                getInt(KEY_SINGLE_CHOICE_ITEMS_RES).let {
                    if (it != 0) {
                        val index = getInt(KEY_SINGLE_CHOICE_CHECKED_ITEM, -1)
                        selectedIndex = index
                        setSingleChoiceItems(
                            it,
                            index,
                            this@AlertDialog
                        )
                    } else
                        getCharSequenceArray(KEY_SINGLE_CHOICE_ITEMS)?.let { items ->
                            val index = getInt(KEY_SINGLE_CHOICE_CHECKED_ITEM, -1)
                            selectedIndex = index
                            setSingleChoiceItems(
                                items,
                                index,
                                this@AlertDialog
                            )
                        }
                }
                getInt(KEY_CUSTOM_VIEW_RES).let { if (it != 0) setView(it) }
            }
            onPrepareDialogBuilder(this)
        }.create().apply {
            setOnShowListener(this@AlertDialog)
        }
    }

    override fun onShow(dialog: DialogInterface?) {
        setButtonColor(DialogInterface.BUTTON_POSITIVE)
        setButtonColor(DialogInterface.BUTTON_NEGATIVE)
        setButtonColor(DialogInterface.BUTTON_NEUTRAL)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE,
            DialogInterface.BUTTON_NEGATIVE,
            DialogInterface.BUTTON_NEUTRAL,
            -> handleButtonClick(which)
            else -> {
                if (selectedIndex != null)
                    selectedIndex = which
                setFragmentResult(
                    REQUEST_KEY_CLICK_ITEM,
                    bundleOf(requestCodePair, KEY_CLICK_ITEM_INDEX to which)
                )

                // This is a single choice callback, if we have not set a positive button let's
                // assume we want to dismiss dialog when an item is selected.
                if (arguments?.getInt(KEY_POSITIVE_BUTTON_RES) == 0 &&
                    arguments?.getCharSequence(KEY_POSITIVE_BUTTON).isNullOrBlank()
                ) {
                    // Simulate a positive click to populate result.
                    handleButtonClick(DialogInterface.BUTTON_POSITIVE)
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE,
            DialogInterface.BUTTON_NEGATIVE,
            DialogInterface.BUTTON_NEUTRAL,
            -> handleButtonClick(which)
            else -> {
                if (isChecked) selectedIndices?.add(which)
                else selectedIndices?.remove(which)
                setFragmentResult(
                    REQUEST_KEY_CLICK_ITEM,
                    bundleOf(
                        requestCodePair,
                        KEY_CLICK_ITEM_INDEX to which,
                        KEY_CLICK_ITEM_INDEX_CHECKED to isChecked,
                    )
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setFragmentResult(
            REQUEST_KEY_DISMISS,
            buildResult(extras).apply { putAll(bundleOf(requestCodePair)) }
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Int.MIN_VALUE
        setFragmentResult(
            REQUEST_KEY_CANCEL,
            buildResult(extras).apply { putAll(bundleOf(requestCodePair)) }
        )
    }

    protected open fun onPrepareDialogBuilder(builder: MaterialAlertDialogBuilder) {}

    protected open fun handleIcon(@DrawableRes icon: Int) =
        handleIcon(ContextCompat.getDrawable(requireContext(), icon))

    protected open fun handleIcon(drawable: Drawable?) =
        arguments?.let { args ->
            drawable?.mutate()?.apply {
                args.getInt(KEY_ICON_COLOR_ATTRIBUTE).let { colorAttribute ->
                    if (colorAttribute != 0)
                        colorFilter =
                            PorterDuffColorFilter(
                                MaterialColors.getColor(
                                    requireContext(),
                                    colorAttribute,
                                    Color.BLACK
                                ),
                                PorterDuff.Mode.SRC_IN
                            )
                    else
                        args.getInt(KEY_ICON_COLOR_RES).let { colorRes ->
                            if (colorRes != 0)
                                colorFilter =
                                    PorterDuffColorFilter(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            colorRes
                                        ),
                                        PorterDuff.Mode.SRC_IN
                                    )
                            else if (args.containsKey(KEY_ICON_COLOR) && args.getInt(KEY_ICON_COLOR) != Int.MIN_VALUE)
                                colorFilter = PorterDuffColorFilter(
                                    args.getInt(KEY_ICON_COLOR),
                                    PorterDuff.Mode.SRC_IN
                                )
                        }
                }
            }
        }

    protected open fun buildResult(bundle: Bundle): Bundle = bundle

    private fun handleButtonClick(which: Int) {
        val requestCodePair = KEY_REQUEST_CODE to requestCode
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                setFragmentResult(
                    REQUEST_KEY_CLICK_POSITIVE,
                    buildResult(extras).apply { putAll(bundleOf(requestCodePair)) }
                )
                if (selectedIndex != null)
                    setFragmentResult(
                        REQUEST_KEY_RESULT_ITEM,
                        buildResult(extras).apply {
                            putAll(
                                bundleOf(
                                    requestCodePair,
                                    KEY_RESULT_ITEM_INDEX to selectedIndex
                                )
                            )
                        }
                    )
                if (selectedIndices != null)
                    setFragmentResult(
                        REQUEST_KEY_RESULT_ITEM,
                        buildResult(extras).apply {
                            putAll(
                                bundleOf(
                                    requestCodePair,
                                    KEY_RESULT_ITEM_INDICES to selectedIndices
                                )
                            )
                        }
                    )
            }
            DialogInterface.BUTTON_NEGATIVE -> setFragmentResult(
                REQUEST_KEY_CLICK_NEGATIVE,
                buildResult(extras).apply { putAll(bundleOf(requestCodePair)) }
            )
            DialogInterface.BUTTON_NEUTRAL -> setFragmentResult(
                REQUEST_KEY_CLICK_NEUTRAL,
                buildResult(extras).apply { putAll(bundleOf(requestCodePair)) }
            )
        }
    }

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
                            else if (arguments?.containsKey(keyButtonColor) == true && arguments?.getInt(
                                    keyButtonColor
                                ) != Int.MIN_VALUE
                            )
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
        const val KEY_ICON_ATTRIBUTE = "iconAttribute"
        const val KEY_ICON_RES = "iconRes"
        const val KEY_ICON_BITMAP = "iconBitmap"
        const val KEY_ICON_COLOR_ATTRIBUTE = "iconColorAttribute"
        const val KEY_ICON_COLOR_RES = "iconColorRes"
        const val KEY_ICON_COLOR = "iconColor"
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
        const val KEY_EXTRAS = "extras"
        const val KEY_REQUEST_CODE = "requestCode"
        const val REQUEST_KEY_DISMISS = "dismiss"
        const val REQUEST_KEY_CANCEL = "cancel"
        const val REQUEST_KEY_CLICK_POSITIVE = "clickPositive"
        const val REQUEST_KEY_CLICK_NEGATIVE = "clickNegative"
        const val REQUEST_KEY_CLICK_NEUTRAL = "clickNeutral"
        const val REQUEST_KEY_CLICK_ITEM = "clickItem"
        const val REQUEST_KEY_RESULT_ITEM = "resultItem"
        const val KEY_CLICK_ITEM_INDEX = "clickItemIndex"
        const val KEY_RESULT_ITEM_INDEX = "resultItemIndex"
        const val KEY_CLICK_ITEM_INDEX_CHECKED = "clickItemIndexChecked"
        const val KEY_RESULT_ITEM_INDICES = "resultItemIndices"

        /**
         * Create a new Alert dialog instance.
         */
        fun newInstance(
            @StringRes titleRes: Int = 0,
            title: CharSequence? = null,
            @StringRes messageRes: Int = 0,
            message: CharSequence? = null,
            @AttrRes iconAttribute: Int = 0,
            @DrawableRes iconRes: Int = 0,
            iconBitmap: Bitmap? = null,
            @AttrRes iconColorAttribute: Int = 0,
            @ColorRes iconColorRes: Int = 0,
            @ColorInt iconColor: Int? = null,
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
            extras: Bundle? = null,
            requestCode: Int = 0,
        ) = AlertDialog().apply {
            arguments = buildArguments(
                titleRes,
                title,
                messageRes,
                message,
                iconAttribute,
                iconRes,
                iconBitmap,
                iconColorAttribute,
                iconColorRes,
                iconColor,
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
                customViewRes,
                extras,
                requestCode,
            )
        }

        @JvmStatic
        protected fun buildArguments(
            @StringRes titleRes: Int = 0,
            title: CharSequence? = null,
            @StringRes messageRes: Int = 0,
            message: CharSequence? = null,
            @AttrRes iconAttribute: Int = 0,
            @DrawableRes iconRes: Int = 0,
            iconBitmap: Bitmap? = null,
            @AttrRes iconColorAttribute: Int = 0,
            @ColorRes iconColorRes: Int = 0,
            @ColorInt iconColor: Int? = null,
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
            extras: Bundle? = null,
            requestCode: Int = 0,
        ) = Bundle().apply {
            if (titleRes != 0) putInt(KEY_TITLE_RES, titleRes)
            putCharSequence(KEY_TITLE, title)
            if (messageRes != 0) putInt(KEY_MESSAGE_RES, messageRes)
            putCharSequence(KEY_MESSAGE, message)
            putInt(KEY_ICON_ATTRIBUTE, iconAttribute)
            putInt(KEY_ICON_RES, iconRes)
            putParcelable(KEY_ICON_BITMAP, iconBitmap)
            if (iconColorAttribute != 0) putInt(KEY_ICON_COLOR_ATTRIBUTE, iconColorAttribute)
            if (iconColorRes != 0) putInt(KEY_ICON_COLOR_RES, iconColorRes)
            iconColor?.let { putInt(KEY_ICON_COLOR, it) }
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
            extras?.let { putBundle(KEY_EXTRAS, it) }
            putInt(KEY_REQUEST_CODE, requestCode)
        }
    }
}

/**
 * Set a listener invoked when dialog is dismissed. A request code that matches the requestCode used
 * to build this dialog is passed as listener argument.
 */
fun Fragment.setDialogDismissListener(listener: (requestCode: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_DISMISS) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE))
    }
}

fun Fragment.setDialogDismissListener(listener: (requestCode: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_DISMISS) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE), bundle)
    }
}

/**
 * Set a listener invoked when dialog is cancelled. A request code that matches the requestCode used
 * to build this dialog is passed as listener argument.
 */
fun Fragment.setDialogCancelListener(listener: (requestCode: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CANCEL) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE))
    }
}

/**
 * Set a listener invoked when dialog is cancelled. A request code that matches the requestCode used
 * to build this dialog is passed as listener argument as well as a [Bundle] containing extra data.
 */
fun Fragment.setDialogCancelListener(listener: (requestCode: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CANCEL) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE), bundle)
    }
}

/**
 * Set a listener invoked when dialog is cancelled or when dialog negative button is clicked. A
 * request code that matches the requestCode used to build this dialog is passed as listener
 * argument.
 */
fun Fragment.setDialogCancelOrNegativeClickListener(listener: (requestCode: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CANCEL) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE))
    }
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_NEGATIVE) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE))
    }
}

/**
 * Set a listener invoked when dialog is cancelled or when dialog negative button is clicked. A
 * request code that matches the requestCode used to build this dialog is passed as listener
 * argument as well as a [Bundle] containing extra data.
 */
fun Fragment.setDialogCancelOrNegativeClickListener(listener: (requestCode: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CANCEL) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE), bundle)
    }
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_NEGATIVE) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE), bundle)
    }
}

/**
 * Set a listener invoked when dialog positive button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument.
 */
fun Fragment.setDialogPositiveClickListener(listener: (requestCode: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_POSITIVE) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE))
    }
}

/**
 * Set a listener invoked when dialog positive button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument as well as a [Bundle]
 * containing extra data.
 */
fun Fragment.setDialogPositiveClickListener(listener: (requestCode: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_POSITIVE) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE), bundle)
    }
}

/**
 * Set a listener invoked when dialog negative button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument.
 */
fun Fragment.setDialogNegativeClickListener(listener: (requestCode: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_NEGATIVE) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE))
    }
}

/**
 * Set a listener invoked when dialog negative button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument as well as a [Bundle]
 * containing extra data.
 */
fun Fragment.setDialogNegativeClickListener(listener: (requestCode: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_NEGATIVE) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE), bundle)
    }
}

/**
 * Set a listener invoked when dialog neutral button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument.
 */
fun Fragment.setDialogNeutralClickListener(listener: (requestCode: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_NEUTRAL) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE))
    }
}

/**
 * Set a listener invoked when dialog neutral button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument as well as a [Bundle]
 * containing extra data.
 */
fun Fragment.setDialogNeutralClickListener(listener: (requestCode: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_NEUTRAL) { _, bundle ->
        listener.invoke(bundle.getInt(AlertDialog.KEY_REQUEST_CODE), bundle)
    }
}

/**
 * Set a listener invoked when dialog single choice item is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument as well as a clickIndex
 * indicating which index was clicked.
 * This is called each time an item is clicked while dialog is opened.
 */
fun Fragment.setDialogSingleChoiceItemClickListener(listener: (requestCode: Int, clickIndex: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX)
        )
    }
}

/**
 * Set a listener invoked when dialog single choice item is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument as well as a clickIndex
 * indicating which index was clicked and a [Bundle] containing extra data.
 * This is called each time an item is clicked while dialog is opened.
 */
fun Fragment.setDialogSingleChoiceItemClickListener(listener: (requestCode: Int, clickIndex: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog single choice item is chosen. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument as well as a clickIndex
 * indicating which index is selected.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun Fragment.setDialogSingleChoiceItemResultListener(listener: (requestCode: Int, resultIndex: Int) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_RESULT_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getInt(AlertDialog.KEY_RESULT_ITEM_INDEX)
        )
    }
}

/**
 * Set a listener invoked when dialog single choice item is chosen. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument as well as a clickIndex
 * indicating which index is selected and a [Bundle] containing extra data.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun Fragment.setDialogSingleChoiceItemResultListener(listener: (requestCode: Int, resultIndex: Int, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_RESULT_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getInt(AlertDialog.KEY_RESULT_ITEM_INDEX),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice item is clicked. A request code that matches
 * the requestCode used to build this dialog is passed as listener argument as well as a clickIndex
 * and checked status indicating which index was clicked and if it is checked.
 * This is called each time an item is clicked while dialog is opened.
 */
fun Fragment.setDialogMultiChoiceItemClickListener(listener: (requestCode: Int, clickIndex: Int, checked: Boolean) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX),
            bundle.getBoolean(AlertDialog.KEY_CLICK_ITEM_INDEX_CHECKED)
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice item is clicked. A request code that matches
 * the requestCode used to build this dialog is passed as listener argument as well as a clickIndex
 * and checked status indicating which index was clicked and if it is checked and a [Bundle]
 * containing extra data.
 * This is called each time an item is clicked while dialog is opened.
 */
fun Fragment.setDialogMultiChoiceItemClickListener(listener: (requestCode: Int, clickIndex: Int, checked: Boolean, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_CLICK_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX),
            bundle.getBoolean(AlertDialog.KEY_CLICK_ITEM_INDEX_CHECKED),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice items are chosen. A request code that matches
 * the requestCode used to build this dialog is passed as listener argument as well as a
 * resultIndices indicating which index are checked.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun Fragment.setDialogMultiChoiceItemsResultListener(listener: (requestCode: Int, resultIndices: IntArray) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_RESULT_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getIntArray(AlertDialog.KEY_RESULT_ITEM_INDICES) ?: intArrayOf(),
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice items are chosen. A request code that matches
 * the requestCode used to build this dialog is passed as listener argument as well as a
 * resultIndices indicating which index are checked and a [Bundle] containing extra data.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun Fragment.setDialogMultiChoiceItemsResultListener(listener: (requestCode: Int, resultIndices: IntArray, bundle: Bundle) -> Unit) {
    setFragmentResultListener(AlertDialog.REQUEST_KEY_RESULT_ITEM) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_REQUEST_CODE),
            bundle.getIntArray(AlertDialog.KEY_RESULT_ITEM_INDICES) ?: intArrayOf(),
            bundle
        )
    }
}
