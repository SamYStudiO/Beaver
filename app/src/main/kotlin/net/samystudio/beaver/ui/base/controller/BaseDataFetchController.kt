package net.samystudio.beaver.ui.base.controller

import android.arch.lifecycle.Observer
import android.view.View
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel

abstract class BaseDataFetchController<VM, D> :
    BaseViewModelController<VM>() where VM : BaseControllerViewModel, VM : DataFetchViewModel<D>
{
    override fun onViewCreated(view: View)
    {
        super.onViewCreated(view)

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

    override fun onDestroyView(view: View)
    {
        super.onDestroyView(view)

        viewModel.dataFetchObservable.removeObservers(this)
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