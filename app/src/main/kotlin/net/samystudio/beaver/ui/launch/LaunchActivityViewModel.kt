package net.samystudio.beaver.ui.launch

import android.os.Bundle
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

@ActivityScope
class LaunchActivityViewModel
@Inject
constructor(fragmentNavigationManager: FragmentNavigationManager) :
    BaseActivityViewModel(fragmentNavigationManager)
{
    override val defaultFragmentClass: Class<out BaseFragment<*>>
        get() = LaunchFragment::class.java
    override val defaultFragmentBundle: Bundle?
        get() = null
}