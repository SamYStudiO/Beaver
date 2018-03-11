@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import android.arch.lifecycle.LiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import net.samystudio.beaver.data.remote.DataRequestState

class RxSingleLiveData<T>
constructor(single: Single<T>? = null) : LiveData<DataRequestState<T>>(), Disposable
{
    var single: Single<T>? = single
        set(value)
        {
            if (single == value) return

            field = value
            disposable?.dispose()
            this.value = null

            if (hasActiveObservers()) refresh()
        }
    var disposable: Disposable? = null

    override fun onActive()
    {
        super.onActive()

        if (value == null || value is DataRequestState.Error) refresh()
    }

    fun refresh()
    {
        if (disposable == null || disposable!!.isDisposed)
            disposable = single
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { postValue(DataRequestState.Start()) }
                ?.subscribe({ data -> postValue(DataRequestState.Success(data)) },
                            { throwable -> postValue(DataRequestState.Error(throwable)) })
    }

    override fun dispose()
    {
        disposable?.dispose()
    }

    // Just to be Disposable compliant, don't care about that value.
    override fun isDisposed(): Boolean = false
}