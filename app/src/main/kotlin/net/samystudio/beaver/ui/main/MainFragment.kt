package net.samystudio.beaver.ui.main

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_main.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class MainFragment : BaseFragment()
{
    override val layoutViewRes: Int = R.layout.fragment_main

    override fun init(savedInstanceState: Bundle?)
    {
        button.setOnClickListener({ startUrl("https://samystudio.github.io/hello") })
    }
}