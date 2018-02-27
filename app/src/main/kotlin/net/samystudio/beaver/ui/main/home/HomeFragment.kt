package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_main.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class HomeFragment : BaseFragment<EmptyViewModel>()
{
    override val viewModelClass: Class<EmptyViewModel> = EmptyViewModel::class.java
    override val layoutViewRes: Int = R.layout.fragment_main

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        button.setOnClickListener({ startUrl("https://samystudio.github.io/hello") })
    }
}