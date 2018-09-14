package net.samystudio.beaver.ui.base.viewmodel

import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.remote.CompletableRequestState

interface DataPushViewModel {
    val dataPushCompletable: LiveData<CompletableRequestState>
}