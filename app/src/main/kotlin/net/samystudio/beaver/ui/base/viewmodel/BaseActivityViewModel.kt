package net.samystudio.beaver.ui.base.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.FragmentManager
import android.view.MenuItem
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseActivityViewModel
constructor(application: Application, fragmentNavigationManager: FragmentNavigationManager) :
    BaseViewControllerViewModel(application, fragmentNavigationManager),
    FragmentManager.OnBackStackChangedListener
{
    abstract val defaultFragmentClass: Class<out BaseFragment<*>>
    abstract val defaultFragmentBundle: Bundle?
    /**
     * @hide
     */
    val result: MutableLiveData<Result> = MutableLiveData()

    init
    {
        @Suppress("LeakingThis")
        fragmentManager.addOnBackStackChangedListener(this)
    }

    @CallSuper
    override fun handleState(intent: Intent, savedInstanceState: Bundle?, arguments: Bundle?)
    {
        if (intent.action == Intent.ACTION_VIEW && !intent.data?.toString().isNullOrBlank())
            onNewUrl(intent.data)
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
                    return clearBackStack()
            }
        }

        return false
    }

    @CallSuper
    override fun setResult(code: Int, intent: Intent?, finish: Boolean)
    {
        result.value = Result(code, intent, finish)
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

    /**
     * @hide
     */
    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}
