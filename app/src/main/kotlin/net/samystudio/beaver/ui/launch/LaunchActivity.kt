package net.samystudio.beaver.ui.launch

import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class LaunchActivity : BaseActivity<LaunchActivityViewModel>()
{
    override val viewModelClass: Class<LaunchActivityViewModel> =
        LaunchActivityViewModel::class.java
    override val layoutViewRes: Int = R.layout.activity_launch
    override val defaultFragmentClass: Class<out BaseFragment<*>> = LaunchFragment::class.java
}
