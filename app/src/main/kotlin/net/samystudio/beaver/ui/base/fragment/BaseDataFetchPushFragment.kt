@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import net.samystudio.beaver.data.remote.CommandRequestState
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataFetchPushFragment<VM, D> :
    BaseDataFetchFragment<VM, D>() where VM : BaseFragmentViewModel, VM : DataFetchViewModel<D>, VM : DataPushViewModel
{
    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        super.onViewModelCreated(savedInstanceState)

        viewModel.dataPushObservable.observe(this, Observer {
            it?.let {
                when (it)
                {
                    is CommandRequestState.Start    -> dataPushStart()
                    is CommandRequestState.Complete ->
                    {
                        dataPushSuccess()
                        dataPushTerminate()
                    }
                    is CommandRequestState.Error    ->
                    {
                        dataPushError(it.error)
                        dataPushTerminate()
                    }
                }
            }
        })
    }

    protected abstract fun dataPushStart()
    protected abstract fun dataPushSuccess()
    protected abstract fun dataPushError(throwable: Throwable)
    protected abstract fun dataPushTerminate()
}
