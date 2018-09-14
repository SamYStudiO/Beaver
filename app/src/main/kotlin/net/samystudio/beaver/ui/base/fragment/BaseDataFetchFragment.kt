package net.samystudio.beaver.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel

abstract class BaseDataFetchFragment<VM, D> :
    BaseViewModelFragment<VM>() where VM : BaseFragmentViewModel, VM : DataFetchViewModel<D> {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataFetchObservable.observe(viewLifecycleOwner, Observer { requestState ->
            requestState?.let {
                when (it) {
                    is DataRequestState.Start -> dataFetchStart()
                    is DataRequestState.Success -> {
                        dataFetchSuccess(it.data)
                        dataFetchTerminate()
                    }
                    is DataRequestState.Error -> {
                        dataFetchError(it.error)
                        dataFetchTerminate()
                    }
                }
            }
        })
    }

    fun refreshData() {
        viewModel.refreshData()
    }

    protected abstract fun dataFetchStart()
    protected abstract fun dataFetchSuccess(data: D)
    protected abstract fun dataFetchError(throwable: Throwable)
    protected abstract fun dataFetchTerminate()
}
