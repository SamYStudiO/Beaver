package net.samystudio.beaver.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveData
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    googleApiAvailabilityManager: GoogleApiAvailabilityManager,
    userManager: UserManager
) : BaseDisposablesViewModel() {
    private val _initializationLiveData =
        // zip all initialization observables required here
        Flowable.zip(
            Observable.timer(1000, TimeUnit.MILLISECONDS).toAsyncState(),
            googleApiAvailabilityManager.availabilityObservable.toAsyncState(),
            { t1, t2 -> if (t2 is AsyncState.Failed) t2 else t1 }
        ).delaySubscription(500, TimeUnit.MILLISECONDS).toTriggerLiveData(true)
            .apply { addTo(disposables) }

    val initializationLiveData: LiveData<AsyncState> = _initializationLiveData
    val userStatusLiveData =
        userManager.statusObservable
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
    val isReady: Boolean
        get() = _initializationLiveData.value is AsyncState.Completed || _initializationLiveData.value is AsyncState.Failed

    fun retry() {
        _initializationLiveData.trigger()
    }
}
