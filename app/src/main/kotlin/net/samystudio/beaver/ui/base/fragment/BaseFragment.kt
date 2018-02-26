@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evernote.android.state.State
import dagger.android.support.DaggerFragment
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.processors.BehaviorProcessor
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.common.navigation.ActivityNavigationManager
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationRequest
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment(), HasSupportFragmentInjector
{
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
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

        init(savedInstanceState)
    }

    override fun onDestroy()
    {
        _titleObservable.onComplete()

        super.onDestroy()
    }

    /**
     * @see ActivityNavigationManager.startActivity
     */
    @JvmOverloads
    fun startActivity(activityClass: Class<out BaseActivity>,
                      extras: Bundle? = null,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false) =
        fragmentNavigationManager.startActivity(activityClass, extras,
                                                options,
                                                forResultRequestCode,
                                                finishCurrentActivity)

    /**
     * @see ActivityNavigationManager.startActivity
     */
    @JvmOverloads
    fun startActivity(intent: Intent,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false) =
        fragmentNavigationManager.startActivity(intent,
                                                options,
                                                forResultRequestCode,
                                                finishCurrentActivity)

    /**
     * @see ActivityNavigationManager.startUrl
     */
    fun startUrl(url: String) = startUrl(Uri.parse(url))

    /**
     * @see ActivityNavigationManager.startUrl
     */
    fun startUrl(uri: Uri) = fragmentNavigationManager.startUrl(uri)

    /**
     * @see FragmentNavigationManager.startFragment
     */
    @JvmOverloads
    fun <T : BaseFragment> startFragment(fragment: T,
                                         bundle: Bundle? = null,
                                         addToBackStack: Boolean = true,
                                         forResultRequestCode: Int? = null) =
        fragmentNavigationManager.startFragment(fragment,
                                                bundle,
                                                addToBackStack,
                                                forResultRequestCode)

    /**
     * @see FragmentNavigationManager.startFragment
     */
    @JvmOverloads
    fun <T : BaseFragment> startFragment(fragmentClass: Class<T>,
                                         bundle: Bundle? = null,
                                         addToBackStack: Boolean = true,
                                         forResultRequestCode: Int? = null) =
        fragmentNavigationManager.startFragment(fragmentClass,
                                                bundle,
                                                addToBackStack,
                                                forResultRequestCode)

    /**
     * @see FragmentNavigationManager.startFragment
     */
    @JvmOverloads
    fun <T : BaseFragment> startFragment(fragmentNavigationRequest: FragmentNavigationRequest<T>,
                                         forResultRequestCode: Int? = null) =
        fragmentNavigationManager.startFragment(fragmentNavigationRequest,
                                                forResultRequestCode)

    /**
     * @see FragmentNavigationManager.clearBackStack
     */
    @JvmOverloads
    fun clearBackStack(@FragmentNavigationManager.StateLossPolicy
                       stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.clearBackStack(stateLossPolicy)

    /**
     * @see FragmentNavigationManager.popBackStack
     */
    @JvmOverloads
    fun popBackStack(@FragmentNavigationManager.StateLossPolicy
                     stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.popBackStack(stateLossPolicy)

    /**
     * @see FragmentNavigationManager.popBackStack
     */
    @JvmOverloads
    fun popBackStack(tag: String,
                     flags: Int,
                     @FragmentNavigationManager.StateLossPolicy
                     stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.popBackStack(tag, flags, stateLossPolicy)

    /**
     * @see FragmentNavigationManager.popBackStack
     */
    @JvmOverloads
    fun popBackStack(@IntRange(from = 1) offset: Int,
                     @FragmentNavigationManager.StateLossPolicy
                     stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.popBackStack(offset, stateLossPolicy)

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
        val fragment = targetFragment as BaseFragment?
        fragment?.onActivityResult(targetRequestCode, code, intent)
    }

    protected abstract fun init(savedInstanceState: Bundle?)
}
