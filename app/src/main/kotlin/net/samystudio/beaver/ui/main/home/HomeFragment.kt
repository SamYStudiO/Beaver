package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import android.view.View
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.fragment_home.*
import net.samystudio.beaver.R
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.ext.genericErrorDialog
import net.samystudio.beaver.ui.base.fragment.BaseDataFetchFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialog
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener
import javax.inject.Inject

class HomeFragment : BaseDataFetchFragment<HomeFragmentViewModel, Home>(), AlertDialogListener
{
    override val layoutViewRes: Int
        get() = R.layout.fragment_home
    override val viewModelClass: Class<HomeFragmentViewModel>
        get() = HomeFragmentViewModel::class.java

    @Inject
    @field:FragmentContext
    protected lateinit var glide: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        invalidate.setOnClickListener({

                                          AlertDialog.Builder(context!!)
                                              .title("title")
                                              .items(R.array.api_urls)
                                              .positiveButton("ok")
                                              .create(this, 11)
                                              .showNow(fragmentManager,
                                                       AlertDialog::class.java.name)

                                          //viewModel.invalidate()
                                      })
    }

    override fun dataFetchStart()
    {
        // TODO show loader
    }

    override fun dataFetchSuccess(data: Home)
    {
        textView.text = data.content
    }

    override fun dataFetchError(throwable: Throwable)
    {
        genericErrorDialog(context!!).showNow(fragmentManager, AlertDialog::class.java.name)
    }

    override fun dataFetchTerminate()
    {
        // TODO hide loader
    }
}