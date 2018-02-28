package net.samystudio.beaver.ui.main.home

import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

@FragmentScope
class HomeFragmentViewModel @Inject constructor(fragmentNavigationManager: FragmentNavigationManager) :
    BaseFragmentViewModel(fragmentNavigationManager)