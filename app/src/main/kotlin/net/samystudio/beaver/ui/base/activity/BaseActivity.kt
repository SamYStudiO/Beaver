@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import dagger.android.support.DaggerAppCompatActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), FragmentManager.OnBackStackChangedListener
{
    @Inject
    protected lateinit var fragmentManager: FragmentManager
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val defaultFragment: Class<out BaseFragment>

    override fun onBackPressed()
    {
        val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null || !currentFragment.onBackPressed())
            super.onBackPressed()
    }

    final override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(layoutViewRes)
        init(savedInstanceState)

        supportFragmentManager.addOnBackStackChangedListener(this)

        // don't need to instantiate fragment when restoring state
        if (savedInstanceState == null)
            fragmentNavigationManager.showFragment(defaultFragment)
    }

    @CallSuper
    override fun onBackStackChanged()
    {
        if (fragmentManager.backStackEntryCount == 0)
            fragmentNavigationManager.showFragment(defaultFragment)
    }

    protected abstract fun init(savedInstanceState: Bundle?)
}
