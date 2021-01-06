package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import net.samystudio.beaver.data.AsyncState

/**
 * LiveData that may trigger a [AsyncState] observable request.
 * @param observable triggered observable
 * @param isTriggeredWhenActivated indicates if observable is triggered when liveData state becomes
 * active, default is true.
 */
class TriggerAsyncStateLiveData(
    val observable: Observable<AsyncState>,
    val isTriggeredWhenActivated: Boolean = true
) : LiveData<AsyncState>(), Disposable {
    private val trigger: PublishSubject<Unit> = PublishSubject.create()
    private val disposable: Disposable =
        trigger.switchMap { _ -> observable.doOnNext { postValue(it) } }.subscribe()

    override fun onActive() {
        super.onActive()
        if (isTriggeredWhenActivated && value !is AsyncState.Started) trigger()
    }

    /**
     * Trigger a new observable request (subscription).
     */
    fun trigger() {
        if (value !is AsyncState.Started)
            trigger.onNext(Unit)
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        disposable.dispose()
    }
}
