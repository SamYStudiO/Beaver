package net.samystudio.beaver.ui.main

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class MainFragment : BaseFragment()
{
    override fun init(savedInstanceState: Bundle?)
    {
    }

    override fun getLayoutViewRes(): Int = R.layout.fragment_main

    override fun getDefaultTitle(): String = ""
}