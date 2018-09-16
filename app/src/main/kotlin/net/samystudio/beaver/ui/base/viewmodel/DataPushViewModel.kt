package net.samystudio.beaver.ui.base.viewmodel

import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.AsyncState

interface DataPushViewModel {
    val dataPushCompletable: LiveData<AsyncState>
}