package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_home.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialog
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener

class HomeFragment : BaseFragment<HomeFragmentViewModel>(),
                     AlertDialogListener
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