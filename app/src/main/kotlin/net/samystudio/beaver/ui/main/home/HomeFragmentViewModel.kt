package net.samystudio.beaver.ui.main.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.HomeApiInterfaceImpl
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.common.viewmodel.ResultAsyncStateLiveData

class HomeFragmentViewModel @ViewModelInject constructor(homeApiInterfaceImpl: HomeApiInterfaceImpl) :
    BaseFragmentViewModel(),
    DataFetchViewModel<Home> {
    private val _dataFetchObservable: ResultAsyncStateLiveData<Home> =
        ResultAsyncStateLiveData(homeApiInterfaceImpl.home()).also { disposables.add(it) }
    override val dataFetchObservable: LiveData<ResultAsyncState<Home>> = _dataFetchObservable

    override fun refreshData() {
        _dataFetchObservable.refresh()
    }
}