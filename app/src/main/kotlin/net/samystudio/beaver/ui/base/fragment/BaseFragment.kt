@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.rxjava3.disposables.CompositeDisposable
import net.samystudio.beaver.data.local.InstanceStateProvider
import net.samystudio.beaver.ext.getClassSimpleTag
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.common.dialog.DialogListener

/**
 * Simple fragment with no injection or view model to avoid boilerplate with screen/dialog that
 * display really simple static content. If you want injection or view model, base class is
 * [BaseViewModelFragment].
 */
abstract class BaseFragment : AppCompatDialogFragment(), DialogInterface.OnShowListener,
    View.OnApplyWindowInsetsListener {
    private val savable = Bundle()
    private val onBackPressCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = onBackPressed()
    }
    private var finished: Boolean = false
    private var dialogDismissed: Boolean = false
    private var bottomSheetWaitingForDismissAllowingStateLoss = false
    protected val navController: NavController
        get() = findNavController()

    // Cannot inject here since we don't have dagger yet.
    protected lateinit var firebaseAnalytics: FirebaseAnalytics
    protected var resultCode: Int by state(Activity.RESULT_CANCELED)
    protected var resultIntent: Intent? by state()
    protected var destroyDisposable: CompositeDisposable? = null
    protected var destroyViewDisposable: CompositeDisposable? = null
    protected var stopDisposable: CompositeDisposable? = null
    protected var pauseDisposable: CompositeDisposable? = null
    var enableBackPressed by state(false, afterSetCallback = { value ->
        onBackPressCallback.isEnabled = value
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
            savable.putAll(savedInstanceState.getBundle(getClassTag()))

        destroyDisposable = CompositeDisposable()
    }

    @CallSuper
    open fun onNewIntent(intent: Intent) {
        childFragmentManager.fragments.forEach {
            (it as? BaseFragment)?.onNewIntent(intent)
            (it as? BasePreferenceFragment)?.onNewIntent(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener(this)
        destroyViewDisposable = CompositeDisposable()
        dialogDismissed = false

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressCallback
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // Before super cause super implementation restore state and may call show, so we need to
        // add listener before.
        if (showsDialog) dialog?.setOnShowListener(this)

        super.onActivityCreated(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext().applicationContext)
    }

    override fun onStart() {
        super.onStart()
        stopDisposable = CompositeDisposable()
    }

    override fun onResume() {
        super.onResume()
        pauseDisposable = CompositeDisposable()

        getScreenTag()?.let {
            firebaseAnalytics.logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                bundleOf(
                    FirebaseAnalytics.Param.SCREEN_NAME to it,
                    FirebaseAnalytics.Param.SCREEN_CLASS to it,
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        pauseDisposable?.dispose()
    }

    override fun onStop() {
        super.onStop()
        stopDisposable?.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(getClassTag(), savable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyViewDisposable?.dispose()
        dialogDismissed = true

        dialog?.let {
            it.setOnShowListener(null)
            it.setOnCancelListener(null)
            it.setOnDismissListener(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable?.dispose()
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsets) = insets

    override fun dismiss() {
        if (dialog !is BottomSheetDialog || !tryDismissWithAnimation(false)) {
            super.dismiss()
        }
    }

    override fun dismissAllowingStateLoss() {
        if (dialog !is BottomSheetDialog || !tryDismissWithAnimation(true)) {
            super.dismissAllowingStateLoss()
        }
    }

    @CallSuper
    override fun onShow(dialog: DialogInterface) {
        (activity as? DialogListener)?.onDialogShow(targetRequestCode)
        (targetFragment as? DialogListener)?.onDialogShow(targetRequestCode)
    }

    @CallSuper
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        setResult(Activity.RESULT_CANCELED, null)
        (activity as? DialogListener)?.onDialogCancel(targetRequestCode)
        (targetFragment as? DialogListener)?.onDialogCancel(targetRequestCode)
    }

    @CallSuper
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        (activity as? DialogListener)?.onDialogDismiss(targetRequestCode)
        (targetFragment as? DialogListener)?.onDialogDismiss(targetRequestCode)

        dialogDismissed = true
        finish()
    }

    /**
     * Override [Activity.onBackPressed], this must be enabled first using [enableBackPressed].
     */
    open fun onBackPressed() {}

    fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

    fun show(manager: FragmentManager) =
        (getClassSimpleTag() + count++).apply { super.show(manager, this) }

    fun setTargetRequestCode(requestCode: Int) {
        setTargetFragment(null, requestCode)
    }

    /**
     * @see Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?) {
        resultCode = code
        resultIntent = intent
    }

    /**
     * Same as [Activity.finish], if [BaseFragment] is a dialog it will be dismissed otherwise
     * [FragmentManager] stack will pop.
     */
    fun finish() {
        if (finished) return

        if (showsDialog && !dialogDismissed) dismiss()
        else {
            if (!showsDialog) navController.popBackStack()

            (activity as? BaseActivity<*>)?.onActivityResult(
                targetRequestCode,
                resultCode,
                resultIntent
            )
            targetFragment?.onActivityResult(targetRequestCode, resultCode, resultIntent)

            finished = true
        }
    }

    /**
     * Tag for google analytics, override if you don't want to use class name.
     */
    protected open fun getScreenTag(): String? = getClassSimpleTag()

    protected fun <T> state(
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) =
        InstanceStateProvider.Nullable(savable, beforeSetCallback, afterSetCallback)

    protected fun <T> state(
        defaultValue: T,
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) =
        InstanceStateProvider.NotNull(
            savable,
            defaultValue,
            beforeSetCallback,
            afterSetCallback
        )

    private fun tryDismissWithAnimation(allowingStateLoss: Boolean): Boolean {
        val baseDialog = dialog
        if (baseDialog is BottomSheetDialog) {
            val behavior: BottomSheetBehavior<*> = baseDialog.behavior
            if (behavior.isHideable && baseDialog.dismissWithAnimation) {
                dismissWithAnimation(behavior, allowingStateLoss)
                return true
            }
        }
        return false
    }

    private fun dismissWithAnimation(
        behavior: BottomSheetBehavior<*>, allowingStateLoss: Boolean
    ) {
        bottomSheetWaitingForDismissAllowingStateLoss = allowingStateLoss
        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            dismissAfterAnimation()
        } else {
            if (dialog is BottomSheetDialog) {
                // We would like to call BottomSheetDialog.removeDefaultCallback here but this is
                // internal function, closest public api is to clear all callbacks.
                @Suppress("DEPRECATION")
                (dialog as BottomSheetDialog).behavior.setBottomSheetCallback(null)
            }
            behavior.addBottomSheetCallback(BottomSheetDismissCallback())
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private fun dismissAfterAnimation() {
        if (bottomSheetWaitingForDismissAllowingStateLoss) {
            super.dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    private inner class BottomSheetDismissCallback : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(
            bottomSheet: View,
            newState: Int
        ) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAfterAnimation()
            }
        }

        override fun onSlide(
            bottomSheet: View,
            slideOffset: Float
        ) {
        }
    }

    companion object {
        private var count: Int = 0
    }
}
