package net.samystudio.beaver.ui.common.dialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.data.toAsyncState
import java.util.concurrent.TimeUnit

class LaunchDialogViewModel @ViewModelInject constructor(
    googleApiAvailabilityManager: GoogleApiAvailabilityManager,
) : ViewModel() {
    private val subject = BehaviorSubject.create<Unit>()
    val initializationObservable =
        subject.switchMap {
            // zip all initialization observables required here
            Observable.timer(1000, TimeUnit.MILLISECONDS).toAsyncState()
                .zipWith(googleApiAvailabilityManager.availabilityObservable.toAsyncState(),
                    { t1, t2 -> if (t2 is AsyncState.Failed) t2 else t1 })
        }.toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()

    init {
        retry()
    }

    fun retry() {
        subject.onNext(Unit)
    }
}
