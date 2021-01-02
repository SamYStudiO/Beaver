package net.samystudio.beaver.ui.main.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.rxjava3.core.BackpressureStrategy
import net.samystudio.beaver.data.remote.HomeApiInterfaceImpl
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel

class HomeFragmentViewModel @ViewModelInject constructor(
    homeApiInterfaceImpl: HomeApiInterfaceImpl
) : BaseDisposablesViewModel() {
    val homeLiveData = LiveDataReactiveStreams.fromPublisher(
        homeApiInterfaceImpl.home().toResultAsyncState().toFlowable(BackpressureStrategy.LATEST)
    )
}
