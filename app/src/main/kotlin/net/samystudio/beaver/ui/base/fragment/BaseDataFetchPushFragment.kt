@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataFetchPushFragment<D, VM> : BaseDataFetchFragment<D, VM>()
        where VM : BaseFragmentViewModel, VM : DataFetchViewModel<D>, VM : DataPushViewModel
{
    abstract fun onPushData()
}