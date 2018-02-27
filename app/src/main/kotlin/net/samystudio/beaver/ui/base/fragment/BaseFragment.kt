@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evernote.android.state.State
import dagger.android.support.DaggerFragment
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.processors.BehaviorProcessor
import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigation
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel> : DaggerFragment(), HasSupportFragmentInjector,
                                                  FragmentNavigation
{
    @Inject
    protected lateinit var viewModelProvider: ViewModelProvider
    protected lateinit var viewModel: VM
    protected abstract val viewModelClass: Class<VM>

    /**
     * @hide
     */
    @Inject
    override lateinit var fragmentNavigationManager: FragmentNavigationManager
    protected val fragmentNavigationManagerIsInitialized
        get() = ::fragmentNavigationManager.isInitialized

    @get:LayoutRes
    protected abstract val layoutViewRes: Int

    private val _titleObservable: BehaviorProcessor<String> = BehaviorProcessor.create()
    val titleObservable: Observable<String> = _titleObservable.toObservable()
    @State
    var title: String = ""
        set(value)
        {
            field = value
            _titleObservable.onNext(value)
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        _titleObservable.onNext(title)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        Timber.d("onCreateView: ")

        return if (layoutViewRes > 0) inflater.inflate(layoutViewRes, container, false)
        else null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider.get(viewModelClass)

        onViewModelCreated(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()

        handleIntent()
    }

    override fun onDestroy()
    {
        _titleObservable.onComplete()

        super.onDestroy()
    }

    /**
     * @hide
     */
    fun onBackPressed(): Boolean
    {
        setResult(Activity.RESULT_CANCELED, null)

        return consumeOnBackPressed()
    }

    /**
     * Override this to catch back key pressed.
     *
     * @return true if you consume event, this means no more parent will catch this event, false
     * if you want parent to handle this event.
     */
    open fun consumeOnBackPressed() = false

    @JvmOverloads
    fun setResult(code: Int, intent: Intent? = null)
    {
        val fragment = targetFragment as BaseFragment<*>?
        fragment?.onActivityResult(targetRequestCode, code, intent)
    }

    @CallSuper
    protected open fun handleIntent()
    {
        val intent = activity?.intent

        if (intent?.action == Intent.ACTION_VIEW && !intent.data?.toString().isNullOrBlank())
            onNewUrl(intent.data)
    }

    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
    }
}
