package net.samystudio.beaver.ui.launch

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_launch.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.main.MainActivity

class LaunchFragment : BaseFragment<LaunchFragmentViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.fragment_launch
    override val viewModelClass: Class<LaunchFragmentViewModel>
        get() = LaunchFragmentViewModel::class.java

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        super.onViewModelCreated(savedInstanceState)

        button.setOnClickListener({
                                      viewModel.startActivity(MainActivity::class.java,
                                                              null,
                                                              null,
                                                              null,
                                                              true)
                                  })
        button2.setOnClickListener({
                                       /*viewModel.titleObservable.value = "hohoho"*/Crashlytics.getInstance()
                                       .crash()
                                   })

        setHasOptionsMenu(true)
    }
}