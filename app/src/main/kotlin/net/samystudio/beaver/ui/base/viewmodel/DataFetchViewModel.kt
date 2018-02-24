@file:Suppress("unused")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData

interface DataFetchViewModel<D>
{
    val liveData: LiveData<D>

    fun fetch()
    fun onFetchSuccess(data: D)
    fun onFetchError(throwable: Throwable? = null) = throwable?.printStackTrace()
}