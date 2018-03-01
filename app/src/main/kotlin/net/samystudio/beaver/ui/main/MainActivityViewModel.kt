package net.samystudio.beaver.ui.main

import android.os.Bundle
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import net.samystudio.beaver.ui.main.home.HomeFragment
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel
@Inject
constructor(fragmentNavigationManager: FragmentNavigationManager) :
    BaseActivityViewModel(fragmentNavigationManager)
{
    override val defaultFragmentClass: Class<out BaseFragment<*>>
        get() = HomeFragment::class.java
    override val defaultFragmentBundle: Bundle?
        get() = null
}