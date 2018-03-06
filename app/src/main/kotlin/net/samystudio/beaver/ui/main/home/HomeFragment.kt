package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_home.*
import net.samystudio.beaver.R
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.ext.GlideRequests
import net.samystudio.beaver.ui.base.fragment.BaseDataFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialog
import javax.inject.Inject

class HomeFragment : BaseDataFragment<HomeFragmentViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.fragment_home
    override val viewModelClass: Class<HomeFragmentViewModel>
        get() = HomeFragmentViewModel::class.java

    @Inject
    @field:FragmentContext
    protected lateinit var glide: GlideRequests

    override fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        viewModel.homeObservable.observe(this, Observer { home ->
            if (home != null)
                data.text = home.data
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        invalidate.setOnClickListener({

                                          AlertDialog.newInstance(AlertDialog.Builder(context!!)
                                                                      .title("title")
                                                                      .message("message")
                                                                      .positiveButton("ok"),
                                                                  this, 11)
                                              .showNow(fragmentManager,
                                                       AlertDialog::class.java.name)

                                          //viewModel.invalidateToken()
                                      })
    }
}