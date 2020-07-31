package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import net.samystudio.beaver.data.ResultAsyncState
import timber.log.Timber

class ResultAsyncStateLiveData<T>(private var bindObservable: Observable<ResultAsyncState<T>>? = null) :
    LiveData<ResultAsyncState<T>>(), Disposable {
    private val trigger: PublishSubject<Unit> = PublishSubject.create()
    private val disposable: Disposable

    init {
        disposable = trigger.switchMap { _ ->
            bindObservable?.doOnNext {
                Timber.d("ResultAsyncStateLiveData: %s", it)
                postValue(it)
            } ?: Observable.just(Unit)
        }.subscribe()
    }

    override fun onActive() {
        super.onActive()

        bindObservable?.let {
            if (value == null || value is ResultAsyncState.Failed
            ) refresh()
        }
    }

    fun refresh() {
        if (value !is ResultAsyncState.Started && bindObservable != null)
            trigger.onNext(Unit)
    }

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() {
        disposable.dispose()
    }
}