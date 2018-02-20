@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseFetchPushDataFragment<D, VM> : BaseDataFetchFragment<D, VM>()
        where VM : BaseViewModel, VM : DataFetchViewModel<D>, VM : DataPushViewModel
{
    abstract fun onPushedData()
}