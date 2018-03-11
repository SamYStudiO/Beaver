@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel

abstract class BaseDataFetchFragment<VM, D> :
    BaseFragment<VM>() where VM : BaseFragmentViewModel, VM : DataFetchViewModel<D>
{
    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        super.onViewModelCreated(savedInstanceState)

        viewModel.dataFetchObservable.observe(this, Observer {
            it?.let {
                when (it)
                {
                    is DataRequestState.Start   -> dataFetchStart()
                    is DataRequestState.Success ->
                    {
                        dataFetchSuccess(it.data)
                        dataFetchTerminate()
                    }
                    is DataRequestState.Error   ->
                    {
                        dataFetchError(it.error)
                        dataFetchTerminate()
                    }
                }
            }
        })
    }

    fun refreshData()
    {
        viewModel.refreshData()
    }

    protected abstract fun dataFetchStart()
    protected abstract fun dataFetchSuccess(data: D)
    protected abstract fun dataFetchError(throwable: Throwable)
    protected abstract fun dataFetchTerminate()
}
