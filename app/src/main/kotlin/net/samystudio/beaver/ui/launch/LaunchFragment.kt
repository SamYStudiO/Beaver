package net.samystudio.beaver.ui.launch

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class LaunchFragment : BaseFragment()
{
    override fun init(savedInstanceState: Bundle?)
    {
    }

    override fun getLayoutViewRes(): Int = R.layout.fragment_launch

    override fun getDefaultTitle(): String = ""
}