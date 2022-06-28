@file:Suppress("unused", "MemberVisibilityCanBePrivate")

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
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
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
    protected val extras
        get() = arguments?.getBundle(KEY_EXTRAS) ?: bundleOf().apply {
            arguments?.putBundle(
                KEY_EXTRAS,
                this
            )
        }
    protected val requestCode
        get() = arguments?.getInt(KEY_REQUEST_CODE) ?: 0

    /**
     * Store current selected index for single choice dialogs.
     */
    protected var selectedIndex: Int? = null

    /**
     * Store current selected indices for multiple choice dialogs.
     */
    protected var selectedIndices: MutableSet<Int>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme).apply {
            arguments?.apply {
                this@AlertDialog.isCancelable = getBoolean(KEY_CANCELABLE, true)

                getInt(KEY_TITLE_RES).let {
                    if (it != 0)
                        setTitle(it)
                    else
                        getCharSequence(KEY_TITLE)?.toString()?.let { title ->
                            setTitle(
                                HtmlCompat.fromHtml(
                                    title,
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                )
                            )
                        }
                }
                getInt(KEY_MESSAGE_RES).let {
                    if (it != 0)
                        setMessage(it)
                    else
                        getCharSequence(KEY_MESSAGE)?.toString()?.let { message ->
                            setMessage(
                                HtmlCompat.fromHtml(
                                    message,
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                )
                            )
                        }
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
                                getParcelable<Bitmap>(KEY_ICON_BITMAP)?.toDrawable(resources)
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
                getInt(KEY_ITEMS_RES).let {
                    selectedIndex = -1
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

    override fun onShow(dialog: DialogInterface) {
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
                    "$REQUEST_KEY_CLICK_ITEM$requestCode",
                    buildResult(extras).apply { putAll(bundleOf(KEY_CLICK_ITEM_INDEX to which)) }
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
                    "$REQUEST_KEY_CLICK_ITEM$requestCode",
                    buildResult(extras).apply {
                        putAll(
                            bundleOf(
                                KEY_CLICK_ITEM_INDEX to which,
                                KEY_CLICK_ITEM_INDEX_CHECKED to isChecked,
                            )
                        )
                    }
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setFragmentResult(
            "$REQUEST_KEY_DISMISS$requestCode",
            buildResult(extras)
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setFragmentResult(
            "$REQUEST_KEY_CANCEL$requestCode",
            buildResult(extras)
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

    protected fun handleButtonClick(which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                setFragmentResult(
                    "$REQUEST_KEY_CLICK_POSITIVE$requestCode",
                    buildResult(extras)
                )
                if (selectedIndex != null)
                    setFragmentResult(
                        "$REQUEST_KEY_RESULT_ITEM$requestCode",
                        buildResult(extras).apply {
                            putAll(
                                bundleOf(
                                    KEY_RESULT_ITEM_INDEX to selectedIndex
                                )
                            )
                        }
                    )
                if (selectedIndices != null)
                    setFragmentResult(
                        "$REQUEST_KEY_RESULT_ITEM$requestCode",
                        buildResult(extras).apply {
                            putAll(
                                bundleOf(
                                    KEY_RESULT_ITEM_INDICES to selectedIndices
                                )
                            )
                        }
                    )
            }
            DialogInterface.BUTTON_NEGATIVE -> setFragmentResult(
                "$REQUEST_KEY_CLICK_NEGATIVE$requestCode",
                buildResult(extras)
            )
            DialogInterface.BUTTON_NEUTRAL -> setFragmentResult(
                "$REQUEST_KEY_CLICK_NEUTRAL$requestCode",
                buildResult(extras)
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
            arguments?.getInt(keyButtonColorStateListRes)?.let { colorStateListRes ->
                if (colorStateListRes != 0) {
                    ContextCompat.getColorStateList(requireContext(), colorStateListRes)
                        .let { colorStateList -> button.setTextColor(colorStateList) }
                } else {
                    arguments?.getParcelable<ColorStateList>(keyButtonColorStateList)
                        ?.let { colorStateList -> button.setTextColor(colorStateList) } ?: run {
                        arguments?.getInt(keyButtonColorRes)?.let { color ->
                            if (color != 0)
                                button.setTextColor(ContextCompat.getColor(requireContext(), color))
                            else if (arguments?.containsKey(keyButtonColor) == true) {
                                arguments?.getInt(keyButtonColor)?.let {
                                    if (it != Int.MIN_VALUE)
                                        button.setTextColor(it)
                                }
                            }
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
    }
}

/**
 * Set a listener invoked when dialog is dismissed.
 */
fun Fragment.setDialogDismissListener(requestCode: Int = 0, listener: (bundle: Bundle) -> Unit) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_DISMISS}$requestCode") { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog is dismissed.
 */
fun AppCompatActivity.setDialogDismissListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_DISMISS}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog is cancelled.
 */
fun Fragment.setDialogCancelListener(requestCode: Int = 0, listener: (bundle: Bundle) -> Unit) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_CANCEL}$requestCode") { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog is cancelled.
 */
fun AppCompatActivity.setDialogCancelListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_CANCEL}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog is cancelled or when dialog negative button is clicked.
 */
fun Fragment.setDialogCancelOrNegativeClickListener(
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    setDialogCancelListener(requestCode) { bundle ->
        listener.invoke(bundle)
    }
    setDialogNegativeClickListener(requestCode) { bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog is cancelled or when dialog negative button is clicked.
 */
fun AppCompatActivity.setDialogCancelOrNegativeClickListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    setDialogCancelListener(navHostId, requestCode) { bundle ->
        listener.invoke(bundle)
    }
    setDialogNegativeClickListener(navHostId, requestCode) { bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog positive button is clicked.
 */
fun Fragment.setDialogPositiveClickListener(
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_CLICK_POSITIVE}$requestCode") { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog positive button is clicked.
 */
fun AppCompatActivity.setDialogPositiveClickListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_CLICK_POSITIVE}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog negative button is clicked.
 */
fun Fragment.setDialogNegativeClickListener(
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_CLICK_NEGATIVE}$requestCode") { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog negative button is clicked.
 */
fun AppCompatActivity.setDialogNegativeClickListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_CLICK_NEGATIVE}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog neutral button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument.
 */
fun Fragment.setDialogNeutralClickListener(
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_CLICK_NEUTRAL}$requestCode") { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog neutral button is clicked. A request code that matches the
 * requestCode used to build this dialog is passed as listener argument.
 */
fun AppCompatActivity.setDialogNeutralClickListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_CLICK_NEUTRAL}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(bundle)
    }
}

/**
 * Set a listener invoked when dialog single choice item is clicked.
 * This is called each time an item is clicked while dialog is opened.
 */
fun Fragment.setDialogSingleChoiceItemClickListener(
    requestCode: Int = 0,
    listener: (clickIndex: Int, bundle: Bundle) -> Unit
) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_CLICK_ITEM}$requestCode") { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog single choice item is clicked.
 * This is called each time an item is clicked while dialog is opened.
 */
fun AppCompatActivity.setDialogSingleChoiceItemClickListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (clickIndex: Int, bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_CLICK_ITEM}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog single choice item is chosen.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun Fragment.setDialogSingleChoiceItemResultListener(
    requestCode: Int = 0,
    listener: (resultIndex: Int, bundle: Bundle) -> Unit
) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_RESULT_ITEM}$requestCode") { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_RESULT_ITEM_INDEX),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog single choice item is chosen.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun AppCompatActivity.setDialogSingleChoiceItemResultListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (resultIndex: Int, bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_RESULT_ITEM}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_RESULT_ITEM_INDEX),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice item is clicked.
 * This is called each time an item is clicked while dialog is opened.
 */
fun Fragment.setDialogMultiChoiceItemClickListener(
    requestCode: Int = 0,
    listener: (clickIndex: Int, checked: Boolean, bundle: Bundle) -> Unit
) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_CLICK_ITEM}$requestCode") { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX),
            bundle.getBoolean(AlertDialog.KEY_CLICK_ITEM_INDEX_CHECKED),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice item is clicked.
 * This is called each time an item is clicked while dialog is opened.
 */
fun AppCompatActivity.setDialogMultiChoiceItemClickListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (clickIndex: Int, checked: Boolean, bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_CLICK_ITEM}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(
            bundle.getInt(AlertDialog.KEY_CLICK_ITEM_INDEX),
            bundle.getBoolean(AlertDialog.KEY_CLICK_ITEM_INDEX_CHECKED),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice items are chosen.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun Fragment.setDialogMultiChoiceItemsResultListener(
    requestCode: Int = 0,
    listener: (resultIndices: IntArray, bundle: Bundle) -> Unit
) {
    setFragmentResultListener("${AlertDialog.REQUEST_KEY_RESULT_ITEM}$requestCode") { _, bundle ->
        listener.invoke(
            bundle.getIntArray(AlertDialog.KEY_RESULT_ITEM_INDICES) ?: intArrayOf(),
            bundle
        )
    }
}

/**
 * Set a listener invoked when dialog multiple choice items are chosen.
 * This is called only once during dialog lifetime and after dialog positive button was clicked.
 */
fun AppCompatActivity.setDialogMultiChoiceItemsResultListener(
    @IdRes navHostId: Int? = null,
    requestCode: Int = 0,
    listener: (resultIndices: IntArray, bundle: Bundle) -> Unit
) {
    run {
        if (navHostId != null)
            supportFragmentManager.findFragmentById(navHostId)
                ?.childFragmentManager
        else supportFragmentManager
    }?.setFragmentResultListener(
        "${AlertDialog.REQUEST_KEY_RESULT_ITEM}$requestCode",
        this
    ) { _, bundle ->
        listener.invoke(
            bundle.getIntArray(AlertDialog.KEY_RESULT_ITEM_INDICES) ?: intArrayOf(),
            bundle
        )
    }
}
