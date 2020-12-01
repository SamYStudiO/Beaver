@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.rxjava3.disposables.CompositeDisposable
import net.samystudio.beaver.data.local.InstanceStateProvider
import net.samystudio.beaver.ext.getClassSimpleTag
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ui.base.activity.BaseActivity

/**
 * Same as [BaseFragment] for [PreferenceFragmentCompat], note this cannot be displayed as dialog
 * though.
 */
abstract class BasePreferenceFragment : PreferenceFragmentCompat(),
    View.OnApplyWindowInsetsListener {
    private val savable = Bundle()
    private val onBackPressCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = onBackPressed()
    }
    private var finished: Boolean = false
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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressCallback
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable?.dispose()
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsets) = insets

    /**
     * Override [Activity.onBackPressed], this must be enabled first using [enableBackPressed].
     */
    open fun onBackPressed() {}

    fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

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
        navController.popBackStack()

        (activity as? BaseActivity<*>)?.onActivityResult(
            targetRequestCode,
            resultCode,
            resultIntent
        )
        targetFragment?.onActivityResult(targetRequestCode, resultCode, resultIntent)

        finished = true
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
        InstanceStateProvider.NotNull(savable, defaultValue, beforeSetCallback, afterSetCallback)
}
