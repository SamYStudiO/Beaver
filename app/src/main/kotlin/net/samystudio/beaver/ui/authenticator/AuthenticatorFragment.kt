package net.samystudio.beaver.ui.authenticator

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_authenticator.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseDataFragment

class AuthenticatorFragment : BaseDataFragment<AuthenticatorFragmentViewModel>()
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
        invalidateToken.setOnClickListener({ viewModel.invalidateToken() })
    }
}