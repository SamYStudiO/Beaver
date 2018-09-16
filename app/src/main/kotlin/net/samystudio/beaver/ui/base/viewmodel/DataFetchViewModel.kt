package net.samystudio.beaver.ui.base.viewmodel

import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.DataAsyncState

interface DataFetchViewModel<T> {
    val dataFetchObservable: LiveData<DataAsyncState<T>>

    fun refreshData()
}