package net.samystudio.beaver.ui.base.viewmodel

import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.ResultAsyncState

interface DataFetchViewModel<T> {
    val dataFetchObservable: LiveData<ResultAsyncState<T>>

    fun refreshData()
}