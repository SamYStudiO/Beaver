package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.manager.HomeRepositoryManager
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.common.viewmodel.ResultAsyncStateLiveData
import javax.inject.Inject

@FragmentScope
class HomeFragmentViewModel @Inject constructor(homeRepositoryManager: HomeRepositoryManager) :
    BaseFragmentViewModel(),
    DataFetchViewModel<Home> {
    private val _dataFetchObservable: ResultAsyncStateLiveData<Home> =
        ResultAsyncStateLiveData(homeRepositoryManager.home())
    override val dataFetchObservable: LiveData<ResultAsyncState<Home>> = _dataFetchObservable

    override fun handleCreate(savedInstanceState: Bundle?) {
        super.handleCreate(savedInstanceState)
        disposables.add(_dataFetchObservable)
    }

    override fun refreshData() {
        _dataFetchObservable.refresh()
    }
}