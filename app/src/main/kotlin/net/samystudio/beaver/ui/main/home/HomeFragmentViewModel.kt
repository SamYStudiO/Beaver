package net.samystudio.beaver.ui.main.home

import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.manager.HomeRepositoryManager
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.common.viewmodel.DataRequestLiveData
import javax.inject.Inject

@FragmentScope
class HomeFragmentViewModel @Inject constructor(homeRepositoryManager: HomeRepositoryManager) :
    BaseFragmentViewModel(),
    DataFetchViewModel<Home> {
    private val _dataFetchObservable: DataRequestLiveData<Home> =
        DataRequestLiveData(homeRepositoryManager.home())
    override val dataFetchObservable: LiveData<DataRequestState<Home>> = _dataFetchObservable

    init {
        disposables.add(_dataFetchObservable)
    }

    override fun refreshData() {
        _dataFetchObservable.refresh()
    }
}