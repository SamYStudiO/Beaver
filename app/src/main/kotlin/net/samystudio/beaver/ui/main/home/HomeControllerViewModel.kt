package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.LiveData
import net.samystudio.beaver.data.manager.HomeRepositoryManager
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.di.scope.ControllerScope
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.common.viewmodel.DataRequestLiveData
import javax.inject.Inject

@ControllerScope
class HomeControllerViewModel
@Inject
constructor(homeRepositoryManager: HomeRepositoryManager) : BaseControllerViewModel(),
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