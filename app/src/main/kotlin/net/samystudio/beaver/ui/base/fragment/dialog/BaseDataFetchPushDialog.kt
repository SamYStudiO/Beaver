@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment.dialog

import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataFetchPushDialog<D, VM> : BaseDataFetchDialog<D, VM>()
        where VM : BaseViewModel, VM : DataFetchViewModel<D>, VM : DataPushViewModel
{
    abstract fun onPushData()
}