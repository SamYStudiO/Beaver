@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import net.samystudio.beaver.ui.common.navigation.FragmentNavigation
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseFragmentViewModel
constructor(override val fragmentNavigationManager: FragmentNavigationManager) : BaseViewModel(),
                                                                                 FragmentNavigation
{
    val title: MutableLiveData<String> = MutableLiveData()

    open fun handleIntent(intent: Intent?)
    {
        if (intent?.action == Intent.ACTION_VIEW && !intent.data?.toString().isNullOrBlank())
            onNewUrl(intent.data)
    }
}