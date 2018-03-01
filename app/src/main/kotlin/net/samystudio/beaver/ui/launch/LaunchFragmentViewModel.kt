package net.samystudio.beaver.ui.launch

import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

@FragmentScope
class LaunchFragmentViewModel
@Inject
constructor(fragmentNavigationManager: FragmentNavigationManager) :
    BaseFragmentViewModel(fragmentNavigationManager)
{
    override val defaultTitle: String?
        get() = "hello mec"
}