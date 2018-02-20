package net.samystudio.beaver.ui.main

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class MainActivity : BaseActivity()
{
    override fun init(savedInstanceState: Bundle?)
    {
    }

    override fun getLayoutViewRes(): Int = R.layout.activity_main

    override fun getDefaultFragment(): Class<out BaseFragment> = MainFragment::class.java
}
