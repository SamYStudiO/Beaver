@file:Suppress("unused")

package net.samystudio.beaver.ui.base.viewmodel

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigation

interface ViewControllerViewModel : FragmentNavigation
{
    //val parent: ViewControllerViewModel?
    val defaultFragmentClass: Class<out BaseFragment<*>>?

    @CallSuper
    fun handleIntent(intent: Intent)
    {
        if (intent.action == Intent.ACTION_VIEW && !intent.data?.toString().isNullOrBlank())
            onNewUrl(intent.data)
    }

    @CallSuper
    fun handleExtras(bundle: Bundle)
    {
    }

    fun handleBackPressed(): Boolean
    {
        val currentFragment: BaseFragment<*>? = getCurrentFragment()

        if (currentFragment != null && currentFragment.onBackPressed())
            return true

        return false
    }

    fun handleBackStackChanged()
    {
        val currentFragment: BaseFragment<*>? = getCurrentFragment()

        if (currentFragment == null)
            defaultFragmentClass?.let { startFragment(it, null, false) }
    }
}