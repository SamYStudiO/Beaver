@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import javax.inject.Inject

abstract class BaseDataFragment<VM : BaseViewModel> : BaseFragment()
{
    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var viewModel: VM

    @CallSuper
    override fun init(savedInstanceState: Bundle?)
    {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
    }

    abstract fun getViewModelClass(): Class<VM>
}
