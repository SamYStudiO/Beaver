@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import dagger.android.support.DaggerAppCompatActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.ActivityNavigationManager
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationRequest
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), FragmentManager.OnBackStackChangedListener
{
    @Inject
    protected lateinit var fragmentManager: FragmentManager
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val defaultFragmentClass: Class<out BaseFragment>

    final override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(layoutViewRes)
        init(savedInstanceState)

        supportFragmentManager.addOnBackStackChangedListener(this)

        // don't need to instantiate fragment when restoring state
        if (savedInstanceState == null)
            fragmentNavigationManager.startFragment(defaultFragmentClass, null, false)
    }

    override fun onNewIntent(intent: Intent?)
    {
        super.onNewIntent(intent)
        setIntent(intent)
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

    override fun onBackPressed()
    {
        val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null || !currentFragment.onBackPressed())
            super.onBackPressed()
    }

    @CallSuper
    override fun onBackStackChanged()
    {
        if (fragmentManager.backStackEntryCount == 0)
            fragmentNavigationManager.startFragment(defaultFragmentClass, null, false)
    }

    protected abstract fun init(savedInstanceState: Bundle?)
}
