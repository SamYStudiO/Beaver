package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_home.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment

class HomeFragment : BaseFragment<HomeFragmentViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.fragment_home
    override val viewModelClass: Class<HomeFragmentViewModel>
        get() = HomeFragmentViewModel::class.java

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        viewModel.homeObservable.observe(this, Observer { home ->
            if (home != null)
                data.text = home.data
        })
    }
}