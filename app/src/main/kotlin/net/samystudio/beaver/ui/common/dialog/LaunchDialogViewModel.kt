package net.samystudio.beaver.ui.common.dialog

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveData
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LaunchDialogViewModel @Inject constructor(
    googleApiAvailabilityManager: GoogleApiAvailabilityManager,
) : BaseDisposablesViewModel() {
    private val _initializationObservable =
        // zip all initialization observables required here
        Observable.timer(1000, TimeUnit.MILLISECONDS).toAsyncState()
            .zipWith(
                googleApiAvailabilityManager.availabilityObservable.toAsyncState(),
                { t1, t2 -> if (t2 is AsyncState.Failed) t2 else t1 }
            )
            .toTriggerLiveData(true)
            .apply { addTo(disposables) }
    val initializationObservable: LiveData<AsyncState> = _initializationObservable

    fun retry() {
        _initializationObservable.trigger()
    }
}
