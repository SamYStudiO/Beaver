package net.samystudio.beaver.ui.common.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxjava3.core.Flowable
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LaunchDialogViewModel @Inject constructor(
    googleApiAvailabilityManager: GoogleApiAvailabilityManager,
) : BaseDisposablesViewModel() {
    private val _initializationProcessor = BehaviorProcessor.create<Unit>()
    val initializationObservable: LiveData<AsyncState> = _initializationProcessor.flatMap {
        // zip all initialization observables required here
        Flowable.timer(1000, TimeUnit.MILLISECONDS).toAsyncState()
            .zipWith(
                googleApiAvailabilityManager.availabilityObservable.toAsyncState()
            ) { t1, t2 -> if (t2 is AsyncState.Failed) t2 else t1 }
    }.toLiveData()

    fun retry() {
        _initializationProcessor.onNext(Unit)
    }
}
