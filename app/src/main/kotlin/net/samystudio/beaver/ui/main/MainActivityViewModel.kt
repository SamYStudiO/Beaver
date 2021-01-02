package net.samystudio.beaver.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel

class MainActivityViewModel @ViewModelInject constructor(userManager: UserManager) :
    BaseDisposablesViewModel() {
    val userStatusLiveData = LiveDataReactiveStreams.fromPublisher(
        userManager.statusObservable.toFlowable(io.reactivex.rxjava3.core.BackpressureStrategy.LATEST)
    )
}
