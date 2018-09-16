package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_home.*
import net.samystudio.beaver.R
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.ext.getGenericErrorDialog
import net.samystudio.beaver.ext.getMethodTag
import net.samystudio.beaver.ext.showIfNonExistent
import net.samystudio.beaver.ui.base.fragment.BaseDataFetchFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorFragment

class HomeFragment : BaseDataFetchFragment<HomeFragmentViewModel, Home>(), AlertDialogListener {
    override val layoutViewRes: Int = R.layout.fragment_home
    override val viewModelClass: Class<HomeFragmentViewModel> = HomeFragmentViewModel::class.java
    override var title: String? = "Home"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        invalidate.setOnClickListener {

            /*AlertDialog.Builder(context!!)
                .title("title")
                .items(R.array.api_urls)
                .positiveButton("ok")
                .create(this, 11)
                .showNow(fragmentManager,
                         AlertDialog::class.java.name)*/

            //viewModel.requestAuthenticator()
            //viewModel.invalidateToken()

            AuthenticatorFragment.newInstance()
                .show(fragmentManager, AuthenticatorFragment::javaClass.name)
            //navigationController.navigate(R.id.action_home_to_authenticator)
        }
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