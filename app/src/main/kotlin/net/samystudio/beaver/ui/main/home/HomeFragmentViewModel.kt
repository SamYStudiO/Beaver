package net.samystudio.beaver.ui.main.home

import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import net.samystudio.beaver.data.remote.HomeApiInterfaceImpl
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    homeApiInterfaceImpl: HomeApiInterfaceImpl
) : BaseDisposablesViewModel() {
    val homeLiveData =
        homeApiInterfaceImpl.home()
            .toResultAsyncState()
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
}
