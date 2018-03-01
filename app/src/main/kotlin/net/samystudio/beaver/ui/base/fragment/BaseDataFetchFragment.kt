@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel

abstract class BaseDataFetchFragment<D, VM> : BaseFragment<VM>()
        where VM : BaseFragmentViewModel, VM : DataFetchViewModel<D>
{
    @CallSuper
    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        super.onViewModelCreated(savedInstanceState)

        viewModel.data.observe(this, Observer { onFetchData(it) })
    }

    open fun fetch()
    {
        viewModel.fetch()
    }

    abstract fun onFetchData(data: D?)
}