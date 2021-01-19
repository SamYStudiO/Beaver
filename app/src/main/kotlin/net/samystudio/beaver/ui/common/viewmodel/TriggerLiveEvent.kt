@file:Suppress("NOTHING_TO_INLINE")

package net.samystudio.beaver.ui.common.viewmodel

import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.ResultAsyncState

/**
 * A [SingleLiveEvent] that may trigger call to a [Flowable] to get some result ([OUT]) at any time
 * using [trigger]. Calling trigger multiple times will cancel previous calls.
 *
 * Note this is a [Disposable] and calling [dispose] is required when you're done with this.
 *
 * @param flowable A [Flowable].
 * @param isTriggeredWhenActivated A [Boolean] that indicates if we want to automatically trigger
 * a new [Flowable] request when this [SingleLiveEvent] becomes active.
 */
class TriggerLiveEvent<OUT>(
    val flowable: Flowable<OUT>,
    val isTriggeredWhenActivated: Boolean = false,
) : SingleLiveEvent<OUT>(), Disposable {
    private val trigger: PublishProcessor<Unit> = PublishProcessor.create()
    private val disposable: Disposable =
        trigger.switchMap { _ -> flowable.doOnNext { postValue(it) } }.subscribe()

    override fun onActive() {
        super.onActive()
        // Only trigger if we asked to and also check OUT is not an AsyncState with Started state.
        if (isTriggeredWhenActivated
            && value !is AsyncState.Started
            && value !is ResultAsyncState.Started<*>
        ) trigger()
    }

    /**
     * Trigger a new request.
     */
    fun trigger() {
        //  Don't need to trigger if we're already in a process to get result (AsyncState.Started).
        if (value !is AsyncState.Started && value !is ResultAsyncState.Started<*>)
            trigger.onNext(Unit)
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        disposable.dispose()
    }
}

inline fun <OUT> Flowable<OUT>.toTriggerLiveEvent(
    isTriggeredWhenActivated: Boolean = false
) = TriggerLiveEvent(this, isTriggeredWhenActivated)

inline fun <OUT> Observable<OUT>.toTriggerLiveEvent(
    isTriggeredWhenActivated: Boolean = false,
    strategy: BackpressureStrategy = BackpressureStrategy.LATEST,
) = toFlowable(strategy).toTriggerLiveEvent(isTriggeredWhenActivated)

inline fun <OUT> Single<OUT>.toTriggerLiveEvent(
    isTriggeredWhenActivated: Boolean = false,
) = toFlowable().toTriggerLiveEvent(isTriggeredWhenActivated)
