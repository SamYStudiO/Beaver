@file:Suppress("unused")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData
import net.samystudio.beaver.data.remote.CommandRequestState

interface DataPushViewModel
{
    val dataPushObservable: LiveData<CommandRequestState>
}