package net.samystudio.beaver.ui.launch

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.main.MainActivity

class LaunchFragment : BaseFragment<LaunchFragmentViewModel>()
{
    override val viewModelClass: Class<LaunchFragmentViewModel> = LaunchFragmentViewModel::class.java
    override val layoutViewRes: Int = R.layout.fragment_launch

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        super.onViewModelCreated(savedInstanceState)

        button.setOnClickListener({
                                      startActivity(MainActivity::class.java,
                                                    null,
                                                    null,
                                                    null,
                                                    true)
                                  })
    }
}