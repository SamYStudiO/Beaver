@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment.dialog

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.CallSuper
import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel

abstract class BaseDataFetchDialog<D, VM> : BaseDataDialog<VM>()
        where VM : BaseViewModel, VM : DataFetchViewModel<D>
{
    @CallSuper
    override fun init(savedInstanceState: Bundle?)
    {
        super.init(savedInstanceState)

        viewModel.liveData.observe(this, Observer { onFetchData(it) })
    }

    open fun fetch()
    {
        viewModel.fetch()
    }

    abstract fun onFetchData(data: D?)
}