package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_main.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class HomeFragment : BaseFragment<HomeFragmentViewModel>()
{
    override val viewModelClass: Class<HomeFragmentViewModel> = HomeFragmentViewModel::class.java
    override val layoutViewRes: Int = R.layout.fragment_main

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        button.setOnClickListener({ viewModel.startUrl("https://samystudio.github.io/hello") })
    }
}