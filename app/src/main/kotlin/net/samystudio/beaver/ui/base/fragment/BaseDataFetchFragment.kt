@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel

abstract class BaseDataFetchFragment<D, VM> : BaseDataFragment<VM>()
        where VM : BaseViewModel, VM : DataFetchViewModel<D>
{
    @CallSuper
    override fun init(savedInstanceState: Bundle?)
    {
        super.init(savedInstanceState)

        viewModel.getLiveData().observe(this, Observer { onFetchedData(it) })
    }

    fun fetch()
    {
        viewModel.fetch()
    }

    abstract fun onFetchedData(data: D?)
}