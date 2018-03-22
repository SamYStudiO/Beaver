package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.LiveData
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.data.remote.api.HomeApiInterfaceManager
import net.samystudio.beaver.di.scope.ControllerScope
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.common.viewmodel.DataRequestLiveData
import javax.inject.Inject

@ControllerScope
class HomeControllerViewModel
@Inject
constructor(homeApiInterfaceManager: HomeApiInterfaceManager) : BaseControllerViewModel(),
                                                                DataFetchViewModel<Home>
{
    private val _dataFetchObservable: DataRequestLiveData<Home> =
        DataRequestLiveData(homeApiInterfaceManager.home())
    override val dataFetchObservable: LiveData<DataRequestState<Home>> = _dataFetchObservable
    override val title: String?
        get() = "home"

    override fun refreshData()
    {
        _dataFetchObservable.refresh()
    }

    fun requestAuthenticator()
    {
        // (activityViewModel as MainActivityViewModel).requestAuthenticator()
    }
}