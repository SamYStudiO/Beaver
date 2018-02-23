package net.samystudio.beaver.ui.main

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class MainActivity : BaseActivity()
{
    override val layoutViewRes: Int = R.layout.activity_main
    override val defaultFragment: Class<out BaseFragment> = MainFragment::class.java

    override fun init(savedInstanceState: Bundle?)
    {
    }
}
