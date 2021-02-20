package net.samystudio.beaver.ui.common.dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveEvent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LaunchDialogViewModel @Inject constructor(
    googleApiAvailabilityManager: GoogleApiAvailabilityManager,
) : ViewModel() {
    val initializationObservable =
        // zip all initialization observables required here
        Observable.timer(1000, TimeUnit.MILLISECONDS).toAsyncState()
            .zipWith(
                googleApiAvailabilityManager.availabilityObservable.toAsyncState(),
                { t1, t2 -> if (t2 is AsyncState.Failed) t2 else t1 }
            )
            .toTriggerLiveEvent(true)

    fun retry() {
        initializationObservable.trigger()
    }
}
