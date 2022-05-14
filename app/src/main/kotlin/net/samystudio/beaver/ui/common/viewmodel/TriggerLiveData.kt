@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.FlowableProcessor
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.ResultAsyncState
import java.util.concurrent.TimeUnit

/**
 * A [LiveData] that may trigger call to a [Flowable] to get some result ([OUT]) at any time
 * using [trigger]. Calling trigger multiple times will cancel previous calls and will be ignored if
 * we already in a process of getting data.
 *
 * Note this is a [Disposable] and calling [dispose] is required when you're done with this.
 *
 * @param isTriggeredWhenActivated A [Boolean] that indicates if we want to automatically trigger
 * a new [Flowable] request when this [LiveData] becomes active.
 * @param throttleTimeout A timeout period while trigger will be ignored after a trigger has be
 * done. Set to 0 to disable this option (0 is default).
 * @param throttleUnit The unit of time for the specified {@code debounceTimeout}
 * @param flowable A [Flowable].
 */
class TriggerLiveData<OUT : Any>(
    flowable: Flowable<OUT>,
    private val isTriggeredWhenActivated: Boolean = false,
    throttleTimeout: Long = 0,
    throttleUnit: TimeUnit = TimeUnit.MILLISECONDS,
    throttleScheduler: Scheduler = Schedulers.computation(),
) : LiveData<OUT>(), Disposable {
    private val trigger: FlowableProcessor<Unit> = PublishProcessor.create()
    private val disposable: Disposable =
        trigger.let {
            if (throttleTimeout > 0)
                it.throttleLatest(throttleTimeout, throttleUnit, throttleScheduler, true)
            else
                it
        }.switchMap {
            flowable.doOnNext { postValue(it) }
        }.subscribe()

    override fun onActive() {
        super.onActive()

        if (isTriggeredWhenActivated) {
            trigger()
        }
    }

    /**
     * Trigger a new request.
     */
    fun trigger() {
        //  Don't need to trigger if we're already in a process to get result.
        if (value !is AsyncState.Started && value !is ResultAsyncState.Started<*>) {
            trigger.onNext(Unit)
        }
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        trigger.onComplete()
        disposable.dispose()
    }
}

inline fun Completable.toTriggerLiveData(
    isTriggeredWhenActivated: Boolean = false,
    debounceTimeout: Long = 0,
    debounceUnit: TimeUnit = TimeUnit.MILLISECONDS,
    debounceScheduler: Scheduler = Schedulers.computation(),
) = toFlowable<Unit>().toTriggerLiveData(
    isTriggeredWhenActivated,
    debounceTimeout,
    debounceUnit,
    debounceScheduler
)

inline fun <T : Any> Single<T>.toTriggerLiveData(
    isTriggeredWhenActivated: Boolean = false,
    debounceTimeout: Long = 0,
    debounceUnit: TimeUnit = TimeUnit.MILLISECONDS,
    debounceScheduler: Scheduler = Schedulers.computation(),
) = toFlowable().toTriggerLiveData(
    isTriggeredWhenActivated,
    debounceTimeout,
    debounceUnit,
    debounceScheduler
)

inline fun <T : Any> Observable<T>.toTriggerLiveData(
    isTriggeredWhenActivated: Boolean = false,
    debounceTimeout: Long = 0,
    debounceUnit: TimeUnit = TimeUnit.MILLISECONDS,
    debounceScheduler: Scheduler = Schedulers.computation(),
    strategy: BackpressureStrategy = BackpressureStrategy.LATEST,
) = toFlowable(strategy).toTriggerLiveData(
    isTriggeredWhenActivated,
    debounceTimeout,
    debounceUnit,
    debounceScheduler
)

inline fun <T : Any> Flowable<T>.toTriggerLiveData(
    isTriggeredWhenActivated: Boolean = false,
    debounceTimeout: Long = 0,
    debounceUnit: TimeUnit = TimeUnit.MILLISECONDS,
    debounceScheduler: Scheduler = Schedulers.computation(),
) = TriggerLiveData(
    this,
    isTriggeredWhenActivated,
    debounceTimeout,
    debounceUnit,
    debounceScheduler
)
