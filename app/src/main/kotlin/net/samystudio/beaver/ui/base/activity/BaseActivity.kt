@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import dagger.android.support.DaggerAppCompatActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigation
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel> : DaggerAppCompatActivity(), FragmentNavigation,
                                                  FragmentManager.OnBackStackChangedListener
{
    @Inject
    protected lateinit var fragmentManager: FragmentManager
    /**
     * @hide
     */
    @Inject
    override lateinit var fragmentNavigationManager: FragmentNavigationManager
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val defaultFragmentClass: Class<out BaseFragment<*>>
    @Inject
    protected lateinit var viewModelProvider: ViewModelProvider
    protected lateinit var viewModel: VM
    protected abstract val viewModelClass: Class<VM>

    final override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider.get(viewModelClass)
        setContentView(layoutViewRes)
        onViewModelCreated(savedInstanceState)

        supportFragmentManager.addOnBackStackChangedListener(this)

        // don't need to instantiate fragment when restoring state
        if (savedInstanceState == null)
            startFragment(defaultFragmentClass, null, false)
    }

    override fun onNewIntent(intent: Intent?)
    {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume()
    {
        super.onResume()

        handleIntent()
    }

    override fun onBackPressed()
    {
        val currentFragment: BaseFragment<*>? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null || !currentFragment.onBackPressed())
            super.onBackPressed()
    }

    @CallSuper
    override fun onBackStackChanged()
    {
        if (fragmentManager.backStackEntryCount == 0)
            startFragment(defaultFragmentClass, null, false)
    }

    @CallSuper
    protected open fun handleIntent()
    {
        if (intent?.action == Intent.ACTION_VIEW && !intent.data?.toString().isNullOrBlank())
            onNewUrl(intent.data)
    }

    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
    }
}
