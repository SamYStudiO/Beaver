package net.samystudio.beaver.ui.main.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.BackpressureStrategy
import net.samystudio.beaver.data.remote.HomeApiInterfaceImpl
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel

class HomeFragmentViewModel @ViewModelInject constructor(
    homeApiInterfaceImpl: HomeApiInterfaceImpl
) : BaseDisposablesViewModel() {
    val homeLiveData =
        homeApiInterfaceImpl.home()
            .toResultAsyncState()
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
}
