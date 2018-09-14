package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import net.samystudio.beaver.data.remote.DataRequestState

class DataRequestLiveData<T>(private var bindObservable: Observable<DataRequestState<T>>? = null) :
    LiveData<DataRequestState<T>>(), Disposable {
    private val trigger: PublishSubject<Unit> = PublishSubject.create()
    private val disposable: Disposable

    init {
        disposable = trigger.flatMap { _ ->
            bindObservable?.doOnNext { postValue(it) } ?: Observable.just(Unit)
        }.subscribe()
    }

    override fun onActive() {
        super.onActive()

        bindObservable?.let { if (value == null || value is DataRequestState.Error) refresh() }
    }

    fun refresh() {
        if (value !is DataRequestState.Start && bindObservable != null)
            trigger.onNext(Unit)
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        disposable.dispose()
    }
}