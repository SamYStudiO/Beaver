package net.samystudio.beaver.ui.base.viewmodel

import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.remote.DataRequestState

interface DataFetchViewModel<T> {
    val dataFetchObservable: LiveData<DataRequestState<T>>

    fun refreshData()
}