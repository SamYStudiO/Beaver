package net.samystudio.beaver.ui.main.home

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.HomeApiInterfaceImpl
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveData
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    homeApiInterfaceImpl: HomeApiInterfaceImpl
) : BaseDisposablesViewModel() {
    private val _homeLiveData =
        homeApiInterfaceImpl.home()
            .toResultAsyncState()
            .toTriggerLiveData()
    val homeLiveData: LiveData<ResultAsyncState<Home>> = _homeLiveData
}
