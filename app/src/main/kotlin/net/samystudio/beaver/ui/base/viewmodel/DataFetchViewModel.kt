@file:Suppress("unused")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData

interface DataFetchViewModel<D>
{
    fun fetch()

    fun getLiveData(): LiveData<D>

    fun onFetchSuccess(data: D)

    fun onFetchError()
    {
    }

    fun onFetchError(throwable: Throwable) = throwable.printStackTrace()
}