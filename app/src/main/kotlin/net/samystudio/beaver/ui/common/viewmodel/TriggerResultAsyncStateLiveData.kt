package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import net.samystudio.beaver.data.ResultAsyncState

/**
 * LiveData that may trigger a [ResultAsyncState] observable request.
 * @param observable triggered observable
 * @param isTriggeredWhenActivated indicates if observable is triggered when liveData state becomes
 * active, default is true.
 */
class TriggerResultAsyncStateLiveData<T>(
    val observable: Observable<ResultAsyncState<T>>,
    val isTriggeredWhenActivated: Boolean = true
) : LiveData<ResultAsyncState<T>>(), Disposable {
    private val trigger: PublishSubject<Unit> = PublishSubject.create()
    private val disposable: Disposable =
        trigger.switchMap { _ -> observable.doOnNext { postValue(it) } }.subscribe()

    override fun onActive() {
        super.onActive()
        if (isTriggeredWhenActivated && value !is ResultAsyncState.Started<T>) trigger()
    }

    /**
     * Trigger a new observable request (subscription).
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
