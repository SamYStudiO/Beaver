package net.samystudio.beaver.ui.base.controller

import android.arch.lifecycle.Observer
import android.view.View
import net.samystudio.beaver.data.remote.CompletableRequestState
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataFetchPushController<VM, D> :
    BaseDataFetchController<VM, D>() where VM : BaseControllerViewModel, VM : DataFetchViewModel<D>, VM : DataPushViewModel
{
    override fun onViewCreated(view: View)
    {
        super.onViewCreated(view)

        viewModel.dataPushCompletable.observe(this, Observer {
            it?.let {
                when (it)
                {
                    is CompletableRequestState.Start    -> dataPushStart()
                    is CompletableRequestState.Complete ->
                    {
                        dataPushSuccess()
                        dataPushTerminate()
                    }
                    is CompletableRequestState.Error    ->
                    {
                        dataPushError(it.error)
                        dataPushTerminate()
                    }
                }
            }
        })
    }

    override fun onDestroyView(view: View)
    {
        super.onDestroyView(view)

        viewModel.dataPushCompletable.removeObservers(this)
    }

    protected abstract fun dataPushStart()
    protected abstract fun dataPushSuccess()
    protected abstract fun dataPushError(throwable: Throwable)
    protected abstract fun dataPushTerminate()
}