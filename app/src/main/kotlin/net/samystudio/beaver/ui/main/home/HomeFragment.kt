package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_home.*
import net.samystudio.beaver.R
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.ext.getGenericErrorDialog
import net.samystudio.beaver.ext.getMethodTag
import net.samystudio.beaver.ext.showIfNonExistent
import net.samystudio.beaver.ui.base.fragment.BaseDataFetchFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener

class HomeFragment : BaseDataFetchFragment<HomeFragmentViewModel, Home>(), AlertDialogListener {
    override val viewModel by viewModels<HomeFragmentViewModel>()
    override val layoutViewRes = R.layout.fragment_home


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        invalidate.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_to_userProfile))
    }

    override fun dataFetchStart() {
        // TODO show loader
    }

    override fun dataFetchSuccess(data: Home) {
        text_view.text = data.content
    }

    override fun dataFetchError(throwable: Throwable) {
        fragmentManager?.let {
            getGenericErrorDialog(context!!).showIfNonExistent(it, getMethodTag())
        }
    }

    override fun dataFetchTerminate() {
        // TODO hide loader
    }
}