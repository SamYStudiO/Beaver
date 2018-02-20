package net.samystudio.beaver.ui.launch

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class LaunchActivity : BaseActivity()
{
    override fun init(savedInstanceState: Bundle?)
    {
    }

    override fun getLayoutViewRes(): Int = R.layout.activity_launch

    override fun getDefaultFragment(): Class<out BaseFragment> = LaunchFragment::class.java
}
