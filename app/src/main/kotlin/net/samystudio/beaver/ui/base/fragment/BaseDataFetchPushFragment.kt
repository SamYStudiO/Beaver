package net.samystudio.beaver.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataFetchPushFragment<VB : ViewBinding, VM, D> :
    BaseDataFetchFragment<VB, VM, D>() where VM : BaseFragmentViewModel, VM : DataFetchViewModel<D>, VM : DataPushViewModel {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataPushCompletable.observe(viewLifecycleOwner, { requestState ->
            requestState?.let {
                when (it) {
                    is AsyncState.Started -> dataPushStart()
                    is AsyncState.Completed -> {
                        dataPushSuccess()
                        dataPushTerminate()
                    }
                    is AsyncState.Failed -> {
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
