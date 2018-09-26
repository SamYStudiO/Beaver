package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.Flowable
import io.reactivex.Observable
import net.samystudio.beaver.data.AsyncState

class AsyncStateLiveData : LiveData<AsyncState>() {
    fun bind(observable: Observable<AsyncState>): Observable<AsyncState> =
        observable.doOnNext { postValue(it) }

    fun bind(flowable: Flowable<AsyncState>): Flowable<AsyncState> =
        flowable.doOnNext { postValue(it) }
}