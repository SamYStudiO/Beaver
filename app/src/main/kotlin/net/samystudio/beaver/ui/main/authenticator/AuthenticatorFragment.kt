package net.samystudio.beaver.ui.main.authenticator

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.fragment_authenticator.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ext.getGenericErrorDialog
import net.samystudio.beaver.ui.base.fragment.BaseDataPushFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialog

class AuthenticatorFragment : BaseDataPushFragment<AuthenticatorFragmentViewModel>() {
    override val layoutViewRes: Int = R.layout.fragment_authenticator
    override val viewModelClass: Class<AuthenticatorFragmentViewModel> =
        AuthenticatorFragmentViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addUserFlow(
            sign_in.clicks().map { AuthenticatorUserFlow.SignIn("hello", "world") })
        viewModel.addUserFlow(
            sign_up.clicks().map { AuthenticatorUserFlow.SignUp("hello", "world") })
    }

    override fun dataPushStart() {
        // TODO show loader
    }

    override fun dataPushSuccess() {
    }

    override fun dataPushError(throwable: Throwable) {
        getGenericErrorDialog(context!!).showNow(fragmentManager, AlertDialog::class.java.name)
    }

    override fun dataPushTerminate() {
        // TODO hide loader
    }

    companion object {
        fun newInstance(requestCode: Int? = null): AuthenticatorFragment {
            val fragment = AuthenticatorFragment()
            requestCode?.let { fragment.targetRequestCode = requestCode }
            fragment.setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_TITLE, 0)
            return fragment
        }
    }
}