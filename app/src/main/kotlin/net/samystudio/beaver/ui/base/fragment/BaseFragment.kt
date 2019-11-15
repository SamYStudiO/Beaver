@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import net.samystudio.beaver.data.local.InstanceStateProvider
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
    private var finished: Boolean = false
    private var dialogDismissed: Boolean = false
    private var restoringState: Boolean = false
    private val savable = Bundle()
    private val onBackPressCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = onBackPressed()
    }
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected val navController: NavController by lazy { findNavController() }
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
    }

    @CallSuper
    open fun onNewIntent(intent: Intent) {
        childFragmentManager.fragments.forEach {
            (it as? BaseFragment)?.onNewIntent(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layoutViewRes > 0) inflater.inflate(layoutViewRes, container, false)
        else null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener(this)
        destroyViewDisposable = CompositeDisposable()
        dialogDismissed = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let { firebaseAnalytics = FirebaseAnalytics.getInstance(it) }

        if (showsDialog) dialog?.setOnShowListener(this)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        restoringState = true
    }

    override fun onStart() {
        super.onStart()
        stopDisposable = CompositeDisposable()
    }

    override fun onResume() {
        super.onResume()
        pauseDisposable = CompositeDisposable()

        if (!restoringState) {
            firebaseAnalytics.setCurrentScreen(
                requireActivity(),
                getClassTag(),
                getClassTag()
            )
            restoringState = false
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
        outState.putBundle(getClassTag(), savable)

        super.onSaveInstanceState(outState)
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
    override fun onShow(dialog: DialogInterface?) {
        (activity as? DialogListener)?.onDialogShow(targetRequestCode)
        (targetFragment as? DialogListener)?.onDialogShow(targetRequestCode)
    }

    @CallSuper
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        (activity as? DialogListener)?.onDialogCancel(targetRequestCode)
        (targetFragment as? DialogListener)?.onDialogCancel(targetRequestCode)

        setResult(Activity.RESULT_CANCELED, null)
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
}
