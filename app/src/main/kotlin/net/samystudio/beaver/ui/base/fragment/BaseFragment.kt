@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
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
    private var restoringState: Boolean = false
    protected val navController: NavController
        get() = findNavController()

    // Cannot inject here since we don't have dagger yet
    protected lateinit var firebaseAnalytics: FirebaseAnalytics
    protected var resultCode: Int by state(Activity.RESULT_CANCELED)
    protected var resultIntent: Intent? by state()
    protected var destroyDisposable: CompositeDisposable? = null
    protected var destroyViewDisposable: CompositeDisposable? = null
    protected var stopDisposable: CompositeDisposable? = null
    protected var pauseDisposable: CompositeDisposable? = null
    var enableBackPressed by state(false, { value -> onBackPressCallback.isEnabled = value })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
            savable.putAll(savedInstanceState.getBundle(getClassTag()))

        destroyDisposable = CompositeDisposable()
        restoringState = savedInstanceState != null
    }

    @CallSuper
    open fun onNewIntent(intent: Intent) {
        childFragmentManager.fragments.forEach {
            (it as? BaseFragment)?.onNewIntent(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener(this)
        destroyViewDisposable = CompositeDisposable()
        dialogDismissed = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // Before super cause super implementation restore state and may call show, so we need to
        // add listener before.
        if (showsDialog) dialog?.setOnShowListener(this)

        super.onActivityCreated(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressCallback)
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext().applicationContext)
    }

    override fun onStart() {
        super.onStart()
        stopDisposable = CompositeDisposable()
    }

    override fun onResume() {
        super.onResume()
        pauseDisposable = CompositeDisposable()

        firebaseAnalytics.setCurrentScreen(
            requireActivity(),
            getClassSimpleTag(),
            null
        )
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

    fun show(manager: FragmentManager) =
        (getClassSimpleTag() + count++).apply { super.show(manager, this) }

    fun setTargetRequestCode(requestCode: Int) {
        setTargetFragment(null, requestCode)
    }

    /**
     * @see [android.app.Activity.setResult]
     */
    fun setResult(code: Int, intent: Intent?) {
        resultCode = code
        resultIntent = intent
    }

    /**
     * Same as [android.app.Activity.finish], if [BaseFragment] is a dialog it will be dismissed
     * otherwise [androidx.fragment.app.FragmentManager] stack will pop.
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

    protected fun <T> state(setterCallback: ((value: T) -> Unit)? = null) =
        InstanceStateProvider.Nullable(savable, setterCallback)

    protected fun <T> state(defaultValue: T, setterCallback: ((value: T) -> Unit)? = null) =
        InstanceStateProvider.NotNull(savable, defaultValue, setterCallback)

    companion object {
        private var count: Int = 0
    }
}
