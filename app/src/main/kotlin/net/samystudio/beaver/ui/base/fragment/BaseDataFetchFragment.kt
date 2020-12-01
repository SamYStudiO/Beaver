package net.samystudio.beaver.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import timber.log.Timber

abstract class BaseDataFetchFragment<VB : ViewBinding, VM, D> :
    BaseViewModelFragment<VB, VM>() where VM : BaseFragmentViewModel, VM : DataFetchViewModel<D> {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataFetchObservable.observe(viewLifecycleOwner, { requestState ->
            requestState?.let {
                Timber.d("onViewCreated: %s", it)
                when (it) {
                    is ResultAsyncState.Started -> dataFetchStart()
                    is ResultAsyncState.Completed -> {
                        dataFetchSuccess(it.data)
                        dataFetchTerminate()
                    }
                    is ResultAsyncState.Failed -> {
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
