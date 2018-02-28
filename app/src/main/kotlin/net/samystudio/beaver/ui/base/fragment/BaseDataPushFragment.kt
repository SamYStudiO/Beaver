@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataPushFragment<D, VM> : BaseFragment<VM>()
        where VM : BaseFragmentViewModel, VM : DataPushViewModel
{
    abstract fun onPushData()
}