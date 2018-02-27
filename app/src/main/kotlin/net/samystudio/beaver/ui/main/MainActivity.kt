package net.samystudio.beaver.ui.main

import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.main.home.HomeFragment

class MainActivity : BaseActivity<MainActivityViewModel>()
{
    override val viewModelClass: Class<MainActivityViewModel> = MainActivityViewModel::class.java
    override val layoutViewRes: Int = R.layout.activity_main
    override val defaultFragmentClass: Class<out BaseFragment<*>> = HomeFragment::class.java
}
