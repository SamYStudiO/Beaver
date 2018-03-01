package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_main.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class HomeFragment : BaseFragment<HomeFragmentViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.fragment_main
    override val viewModelClass: Class<HomeFragmentViewModel>
        get() = HomeFragmentViewModel::class.java

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        button.setOnClickListener({ viewModel.startUrl("https://samystudio.github.io/hello") })
    }
}