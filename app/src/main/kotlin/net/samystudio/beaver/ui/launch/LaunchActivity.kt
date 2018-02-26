package net.samystudio.beaver.ui.launch

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class LaunchActivity : BaseActivity()
{
    override val layoutViewRes: Int = R.layout.activity_launch
    override val defaultFragmentClass: Class<out BaseFragment> = LaunchFragment::class.java

    override fun init(savedInstanceState: Bundle?)
    {
    }
}
