package net.samystudio.beaver.ui.launch

import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseDataFragment

class LaunchFragment : BaseDataFragment<LaunchViewModel>()
{
    override val viewModelClass: Class<LaunchViewModel> = LaunchViewModel::class.java
    override val layoutViewRes: Int = R.layout.fragment_launch
    override val defaultTitle: String = ""
}