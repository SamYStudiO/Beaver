package net.samystudio.beaver.ui.base.controller

import android.arch.lifecycle.Observer
import net.samystudio.beaver.data.remote.CompletableRequestState
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataPushFragment<VM> :
    BaseController<VM>() where VM : BaseControllerViewModel, VM : DataPushViewModel
{
    override fun onViewModelCreated()
    {
        super.onViewModelCreated()

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

    protected abstract fun dataPushStart()
    protected abstract fun dataPushSuccess()
    protected abstract fun dataPushError(throwable: Throwable)
    protected abstract fun dataPushTerminate()
}