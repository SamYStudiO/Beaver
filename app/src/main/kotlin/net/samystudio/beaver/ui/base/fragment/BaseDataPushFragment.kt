package net.samystudio.beaver.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataPushFragment<VB : ViewBinding, VM> :
    BaseViewModelFragment<VB, VM>() where VM : BaseFragmentViewModel, VM : DataPushViewModel {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataPushCompletable.observe(viewLifecycleOwner, Observer { requestState ->
            requestState?.let {
                when (it) {
                    is AsyncState.Started -> dataPushStart()
                    is AsyncState.Completed -> dataPushSuccess()
                    is AsyncState.Failed -> dataPushError(it.error)
                    is AsyncState.Canceled -> dataPushCanceled()
                    is AsyncState.Terminate -> dataPushTerminate()
                }
            }
        })
    }

    protected abstract fun dataPushStart()
    protected abstract fun dataPushSuccess()
    protected abstract fun dataPushError(throwable: Throwable)
    protected abstract fun dataPushCanceled()
    protected abstract fun dataPushTerminate()
}
