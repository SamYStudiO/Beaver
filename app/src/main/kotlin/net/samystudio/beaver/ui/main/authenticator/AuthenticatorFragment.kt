package net.samystudio.beaver.ui.main.authenticator

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import kotlinx.android.synthetic.main.fragment_authenticator.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ext.getGenericErrorDialog
import net.samystudio.beaver.ui.base.fragment.BaseDataPushFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialog

class AuthenticatorFragment : BaseDataPushFragment<AuthenticatorFragmentViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.fragment_authenticator
    override val viewModelClass: Class<AuthenticatorFragmentViewModel>
        get() = AuthenticatorFragmentViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        singin.setOnClickListener({ viewModel.signIn("hello", "world") })
        sinup.setOnClickListener({ viewModel.signUp("hello", "world") })
        invalidateToken.setOnClickListener({ })
    }

    override fun dataPushStart()
    {
        // TODO show loader
    }

    override fun dataPushSuccess()
    {
    }

    override fun dataPushError(throwable: Throwable)
    {
        getGenericErrorDialog(context!!).showNow(fragmentManager, AlertDialog::class.java.name)
    }

    override fun dataPushTerminate()
    {
        // TODO hide loader
    }

    companion object
    {
        fun newInstance(requestCode: Int?): AuthenticatorFragment
        {
            val fragment = AuthenticatorFragment()
            requestCode?.let { fragment.setTargetActivity(requestCode) }
            fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0)
            return fragment
        }
    }
}