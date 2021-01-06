package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import net.samystudio.beaver.data.ResultAsyncState

/**
 * LiveData that may trigger a [ResultAsyncState] flowable request.
 * @param flowable triggered flowable
 * @param isTriggeredWhenActivated indicates if flowable is triggered when liveData state becomes
 * active, default is true.
 */
class TriggerResultAsyncStateLiveData<T>(
    val flowable: Flowable<ResultAsyncState<T>>,
    val isTriggeredWhenActivated: Boolean = true
) : LiveData<ResultAsyncState<T>>(), Disposable {
    private val trigger: PublishProcessor<Unit> = PublishProcessor.create()
    private val disposable: Disposable =
        trigger.switchMap { _ -> flowable.doOnNext { postValue(it) } }.subscribe()

    override fun onActive() {
        super.onActive()
        if (isTriggeredWhenActivated && value !is ResultAsyncState.Started<T>) trigger()
    }

    /**
     * Trigger a new flowable request (subscription).
     */
    fun trigger() {
        if (value !is ResultAsyncState.Started<T>)
            trigger.onNext(Unit)
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        disposable.dispose()
    }
}
