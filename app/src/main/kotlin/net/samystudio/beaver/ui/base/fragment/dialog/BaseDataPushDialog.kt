@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment.dialog

import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataPushDialog<D, VM> : BaseDialog<VM>()
        where VM : BaseFragmentViewModel, VM : DataPushViewModel
{
    abstract fun onPushData()
}