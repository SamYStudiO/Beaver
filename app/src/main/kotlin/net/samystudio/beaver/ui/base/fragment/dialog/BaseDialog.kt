@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.base.fragment.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.annotation.StyleRes
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.subjects.PublishSubject
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

/**
 * Same as [android.support.v4.app.DialogFragment] but derived from [BaseFragment] instead of
 * [android.support.v4.app.Fragment] to make integration easier.
 *
 * @see android.support.v4.app.DialogFragment
 */
abstract class BaseDialog : BaseFragment(), DialogInterface.OnCancelListener,
                            DialogInterface.OnDismissListener
{

    private var lateShow: Boolean = false
    private var lateShowBundle: Bundle? = null

    private val _onDismissObservable = PublishSubject.create<DialogInterface>()
    val onDismissObservable: Completable = _onDismissObservable.ignoreElements()

    private val _onCancelObservable = PublishSubject.create<DialogInterface>()
    val onCancelObservable: Maybe<*> = _onCancelObservable.firstElement()
    private var style = STYLE_NORMAL
    private var viewDestroyed: Boolean = false
    private var dismissed: Boolean = false
    @StyleRes
    var theme = 0
        private set
    var dialog: Dialog? = null
        private set
    var cancelable = true
        set(value)
        {
            field = value
            dialog?.setCancelable(value)
        }

    /**
     * Call to customize the basic appearance and behavior of the
     * fragment's dialog.  This can be used for some common dialog behaviors,
     * taking care of selecting flags, theme, and other options for you.  The
     * same effect can be achieve by manually setting Dialog and Window
     * attributes yourself.  Calling this after the fragment's Dialog is
     * created will have no effect.
     *
     * @param style Selects a standard style: may be [STYLE_NORMAL],
     * [STYLE_NO_TITLE], [STYLE_NO_FRAME], or
     * [STYLE_NO_INPUT].
     * @param theme Optional custom theme.  If 0, an appropriate theme (based
     * on the style) will be selected for you.
     */
    fun setStyle(@DialogStyle style: Long, @StyleRes theme: Int)
    {
        this.style = style

        if (this.style == STYLE_NO_FRAME || this.style == STYLE_NO_INPUT)
            this.theme = android.R.style.Theme_Panel

        if (theme != 0)
            this.theme = theme
    }

    @JvmOverloads
    fun show(bundle: Bundle? = null)
    {
        if (fragmentNavigationManagerIsInitialized)
        {
            lateShow = false
            lateShowBundle = null
            fragmentNavigationManager.startFragment(this, bundle)
        }
        else
        {
            lateShow = true
            lateShowBundle = bundle
        }
    }

    @JvmOverloads
    fun dismiss(@FragmentNavigationManager.StateLossPolicy stateLossPolicy: Long? = null)
    {
        if (fragmentNavigationManagerIsInitialized)
            fragmentNavigationManager.dismissDialog(this, stateLossPolicy)
        else
        {
            lateShow = false
            lateShowBundle = null
        }
    }

    private fun dismissInternal()
    {
        if (dismissed)
            return

        dismissed = true

        dialog?.dismiss()
        dialog = null

        viewDestroyed = true

        fragmentNavigationManager.dismissDialog(this)
    }

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)

        dismissed = false
    }

    override fun onDetach()
    {
        super.onDetach()

        dismissed = true
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
        {
            style = savedInstanceState.getLong(SAVED_STYLE, STYLE_NORMAL)
            theme = savedInstanceState.getInt(SAVED_THEME, 0)
            cancelable = savedInstanceState.getBoolean(SAVED_CANCELABLE, true)
        }

        if (lateShow) show(lateShowBundle)
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater
    {
        dialog = onCreateDialog(savedInstanceState)

        dialog?.let {

            setupDialog(it, style)

            // TODO check return scope (let or onGetLayoutInflater ???)
            return dialog?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        return context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    /**
     * @hide
     */
    fun setupDialog(dialog: Dialog, style: Long)
    {
        when (style)
        {
            STYLE_NO_INPUT                 ->
            {
                dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            }

            STYLE_NO_FRAME, STYLE_NO_TITLE -> dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * [onCreateView] does not need
     * to be implemented since the AlertDialog takes care of its own content.
     *
     *
     * This method will be called after [.onCreate] and
     * before [.onCreateView].  The default implementation simply instantiates
     * and returns a [Dialog] class.
     *
     *
     * *Note: DialogFragment own the [Dialog.setOnCancelListener] and
     * [Dialog.setOnDismissListener] callbacks.  You must not set them yourself.*
     * To find out about these events, override [onCancel] and [onDismiss].
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    open fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val dialog = Dialog(activity, theme)

        if (layoutViewRes != 0)
            dialog.setContentView(LayoutInflater
                                      .from(context)
                                      .inflate(layoutViewRes, null, false))

        init(savedInstanceState)

        return dialog
    }

    override fun onCancel(dialog: DialogInterface)
    {
        setResult(Activity.RESULT_CANCELED, null)

        _onCancelObservable.onNext(dialog)
        _onCancelObservable.onComplete()
    }

    override fun onDismiss(dialog: DialogInterface)
    {
        if (!viewDestroyed)
            dismissInternal()

        _onDismissObservable.onNext(dialog)
        _onDismissObservable.onComplete()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        val view = view

        if (view != null)
        {
            if (view.parent != null)
            {
                throw IllegalStateException("DialogFragment can not be attached to a container view")
            }

            dialog?.setContentView(view)
        }

        if (activity != null)
            dialog?.ownerActivity = activity

        dialog?.setCancelable(cancelable)
        dialog?.setOnCancelListener(this)
        dialog?.setOnDismissListener(this)

        val dialogState = savedInstanceState?.getBundle(SAVED_DIALOG_STATE_TAG)

        if (dialogState != null)
            dialog?.onRestoreInstanceState(dialogState)
    }

    override fun onStart()
    {
        super.onStart()

        if (dialog != null)
        {
            viewDestroyed = false
            dialog?.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        if (dialog != null)
        {
            val dialogState = dialog?.onSaveInstanceState()

            if (dialogState != null)
                outState.putBundle(SAVED_DIALOG_STATE_TAG, dialogState)
        }
        if (style != STYLE_NORMAL)
            outState.putLong(SAVED_STYLE, style)

        if (theme != 0)
            outState.putInt(SAVED_THEME, theme)

        if (!cancelable)
            outState.putBoolean(SAVED_CANCELABLE, cancelable)
    }

    override fun onStop()
    {
        super.onStop()

        if (dialog != null)
            dialog?.hide()
    }

    /**
     * Remove dialog.
     */
    override fun onDestroyView()
    {
        super.onDestroyView()

        if (dialog != null)
        {
            // Set removed here because this dismissal is just to hide
            // the dialog -- we don't want this to cause the fragment to
            // actually be removed.
            viewDestroyed = true
            dialog?.dismiss()
            dialog = null
        }
    }

    override fun onDestroy()
    {
        _onDismissObservable.onComplete()
        _onCancelObservable.onComplete()

        super.onDestroy()
    }

    companion object
    {
        @IntDef(STYLE_NORMAL,
                STYLE_NO_TITLE,
                STYLE_NO_FRAME,
                STYLE_NO_INPUT)
        @Retention(AnnotationRetention.SOURCE)
        private annotation class DialogStyle

        /**
         * Style for [setStyle]: a basic, normal dialog.
         */
        const val STYLE_NORMAL = 0L

        /**
         * Style for [setStyle]: don't include a title area.
         */
        const val STYLE_NO_TITLE = 1L

        /**
         * Style for [setStyle]: don't draw
         * any frame at all; the view hierarchy returned by [onCreateView]
         * is entirely responsible for drawing the dialog.
         */
        const val STYLE_NO_FRAME = 2L

        /**
         * Style for [setStyle]: like
         * [STYLE_NO_FRAME], but also disables all input to the dialog.
         * The user can not touch it, and its window will not receive input focus.
         */
        const val STYLE_NO_INPUT = 3L

        private const val SAVED_DIALOG_STATE_TAG = "android:savedDialogState"
        private const val SAVED_STYLE = "android:style"
        private const val SAVED_THEME = "android:theme"
        private const val SAVED_CANCELABLE = "android:cancelable"
    }
}
