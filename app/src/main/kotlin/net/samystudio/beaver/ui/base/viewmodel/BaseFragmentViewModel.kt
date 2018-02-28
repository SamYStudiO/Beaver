@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.MutableLiveData
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseFragmentViewModel
constructor(override val fragmentNavigationManager: FragmentNavigationManager) : BaseViewModel(),
                                                                                 ViewControllerViewModel
{
    val title: MutableLiveData<String> = MutableLiveData()
}