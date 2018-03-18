@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import android.arch.lifecycle.LiveData
import io.reactivex.Flowable
import io.reactivex.Observable
import net.samystudio.beaver.data.remote.CompletableRequestState

class CompletableRequestLiveData : LiveData<CompletableRequestState>()
{
    fun bind(
        observable: Observable<CompletableRequestState>): Observable<CompletableRequestState> =
        observable.doOnNext { postValue(it) }

    fun bind(
        flowable: Flowable<CompletableRequestState>): Flowable<CompletableRequestState> =
        flowable.doOnNext { postValue(it) }
}