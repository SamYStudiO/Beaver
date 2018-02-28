package net.samystudio.beaver.ui.main

import android.content.Intent
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel @Inject constructor() :
    BaseActivityViewModel()
{
    override val defaultFragmentClass: Class<out BaseFragment<*>>?
        get() = TODO("not implemented")
}