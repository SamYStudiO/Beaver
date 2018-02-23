@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import javax.inject.Inject

abstract class BaseDataFragment<VM : BaseViewModel> : BaseFragment()
{
    @Inject
    protected lateinit var viewModelProvider: ViewModelProvider
    protected lateinit var viewModel: VM
    protected abstract val viewModelClass: Class<VM>

    @CallSuper
    override fun init(savedInstanceState: Bundle?)
    {
        viewModel = viewModelProvider.get(viewModelClass)
    }
}
