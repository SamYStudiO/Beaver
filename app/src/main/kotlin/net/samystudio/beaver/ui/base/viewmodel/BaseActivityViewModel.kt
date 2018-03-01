package net.samystudio.beaver.ui.base.viewmodel

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.FragmentManager
import android.view.MenuItem
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseActivityViewModel
constructor(fragmentNavigationManager: FragmentNavigationManager) :
    BaseViewControllerViewModel(fragmentNavigationManager),
    FragmentManager.OnBackStackChangedListener
{
    abstract val defaultFragmentClass: Class<out BaseFragment<*>>
    abstract val defaultFragmentBundle: Bundle?

    init
    {
        @Suppress("LeakingThis")
        fragmentManager.addOnBackStackChangedListener(this)
    }

    @CallSuper
    override fun handleRestoreState(intent: Intent, savedInstanceState: Bundle?, arguments: Bundle?)
    {
        if (intent.action == Intent.ACTION_VIEW && !intent.data?.toString().isNullOrBlank())
            onNewUrl(intent.data)
    }

    override fun handleReady()
    {
        onBackStackChanged()
    }

    override fun handleBackPressed(): Boolean
    {
        val currentFragment: BaseFragment<*>? = getCurrentFragment()

        if (currentFragment != null && currentFragment.onBackPressed())
            return true

        return false
    }

    override fun handleOptionsItemSelected(item: MenuItem): Boolean
    {
        val currentFragment: BaseFragment<*>? = getCurrentFragment()

        if (currentFragment == null || !currentFragment.willConsumeOptionsItem(item))
        {
            when (item.itemId)
            {
                android.R.id.home ->
                {
                    clearBackStack()
                    return true
                }
            }
        }

        return false
    }

    override fun onBackStackChanged()
    {
        val currentFragment: BaseFragment<*>? = getCurrentFragment()

        if (currentFragment == null)
            startFragment(defaultFragmentClass, defaultFragmentBundle, false)
    }

    override fun onCleared()
    {
        super.onCleared()
        fragmentManager.removeOnBackStackChangedListener(this)
    }
}
