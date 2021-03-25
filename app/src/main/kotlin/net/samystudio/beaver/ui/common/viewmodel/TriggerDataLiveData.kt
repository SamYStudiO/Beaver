@file:Suppress("MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.ResultAsyncState

/**
 * A [LiveData] that may trigger call to a [Flowable] with optional data ([IN]) to get some result
 * ([OUT]) at any time using [trigger]. Calling trigger multiple times will cancel previous calls.
 *
 * Note this is a [Disposable] and calling [dispose] is required when you're done with this.
 *
 * @param isTriggeredWhenActivated A [Boolean] that indicates if we want to automatically trigger
 * a new [Flowable] request when this [LiveData] becomes active. You may want to set [defaultData]
 * as well to define data ([IN]) for this request.
 * @param defaultData Data ([IN]) pass to [Flowable] when this [SingleLiveEvent] is getting active,
 * this is useless if [isTriggeredWhenActivated] is false as nothing we'll be trigger when this
 * getting active.
 * @param flowable A lambda to get triggered [Flowable] with data ([IN]).
 */
class TriggerDataLiveData<IN, OUT>(
    val isTriggeredWhenActivated: Boolean = false,
    val defaultData: IN? = null,
    val flowable: (IN) -> Flowable<OUT>,
) : LiveData<OUT>(), Disposable {
    private val trigger: PublishProcessor<IN> = PublishProcessor.create()
    private val disposable: Disposable =
        trigger.switchMap { data -> flowable.invoke(data).doOnNext { postValue(it) } }.subscribe()

    override fun onActive() {
        super.onActive()
        // Only trigger if we asked to and also check OUT is not an AsyncState with Started state.
        if (isTriggeredWhenActivated &&
            defaultData != null &&
            value !is AsyncState.Started &&
            value !is ResultAsyncState.Started<*>
        ) trigger(defaultData)
    }

    /**
     * Trigger a new request.
     */
    fun trigger(data: IN) {
        //  Don't need to trigger if we're already in a process to get result (AsyncState.Started).
        if (value !is AsyncState.Started && value !is ResultAsyncState.Started<*>)
            trigger.onNext(data)
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        disposable.dispose()
    }
}

inline fun <IN, OUT> ((IN) -> Flowable<OUT>).toTriggerDataLiveData(
    isTriggeredWhenActivated: Boolean = false,
    defaultData: IN? = null,
) = TriggerDataLiveData(isTriggeredWhenActivated, defaultData, this)
