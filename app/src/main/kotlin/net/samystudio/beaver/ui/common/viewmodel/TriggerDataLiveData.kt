@file:Suppress("NOTHING_TO_INLINE")

package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.ResultAsyncState
import java.util.concurrent.TimeUnit

/**
 * A [LiveData] that may trigger call to a [Flowable] with optional data ([IN]) to get some
 * result ([OUT]) at any time using [trigger]. Calling trigger multiple times will cancel previous
 * calls and will be ignored if we already in a process of getting data.
 *
 * Note this is a [Disposable] and calling [dispose] is required when you're done with this.
 *
 * @param isTriggeredWhenActivated A [Boolean] that indicates if we want to automatically trigger
 * a new [Flowable] request when this [LiveData] becomes active. You may want to set
 * {@code initialData} as well to define data ([IN]) for this request for the first activation.
 * @param initialDataWhenActivated Data ([IN]) pass to [Flowable] when this [LiveData] is getting active,
 * this is useless if {@code isTriggeredWhenActivated} is false as nothing we'll be trigger when this
 * getting active.
 * @param throttleTimeout A timeout period while trigger will be ignored after a trigger has be
 * done. Set to 0 to disable this option (0 is default).
 * @param throttleUnit The unit of time for the specified {@code debounceTimeout}
 * @param flowableBuilder A lambda to get triggered [Flowable] with data ([IN]).
 */
open class TriggerDataLiveData<IN : Any, OUT : Any>(
    private val isTriggeredWhenActivated: Boolean = false,
    private val initialDataWhenActivated: IN? = null,
    private val useLastLoadDataWhenActivated: Boolean = true,
    throttleTimeout: Long = 0,
    throttleUnit: TimeUnit = TimeUnit.MILLISECONDS,
    throttleScheduler: Scheduler = Schedulers.computation(),
    val flowableBuilder: (IN) -> Flowable<OUT>,
) : LiveData<OUT>(), Disposable {
    private val trigger = PublishProcessor.create<IN>()
    private val disposable: Disposable =
        trigger.let {
            if (throttleTimeout > 0)
                it.throttleLatest(throttleTimeout, throttleUnit, throttleScheduler, true)
            else
                it
        }.switchMap { data ->
            flowableBuilder(data).doOnNext { postValue(it) }
        }.subscribe()
    private var lastData: IN? = null

    override fun onActive() {
        super.onActive()

        if (isTriggeredWhenActivated)
            run {
                lastData?.takeIf { useLastLoadDataWhenActivated }
                    ?: initialDataWhenActivated
            }?.let { trigger(it) }
    }

    /**
     * Trigger a new request.
     */
    fun trigger(data: IN) {
        //  Don't need to trigger if we're already in a process to get result.
        if (data != lastData || (value !is AsyncState.Started && value !is ResultAsyncState.Started<*>)) {
            lastData = data
            trigger.onNext(data)
        }
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        disposable.dispose()
    }
}
