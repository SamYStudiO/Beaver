package net.samystudio.beaver.ui.authenticator

import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity

class AuthenticatorActivity : BaseActivity<AuthenticatorActivityViewModel>()
{
    override val layoutViewRes: Int
        get() = R.layout.activity_account_authenticator
    override val viewModelClass: Class<AuthenticatorActivityViewModel>
        get() = AuthenticatorActivityViewModel::class.java

    override fun finish()
    {
        viewModel.handleFinish(isTaskRoot)

        super.finish()
    }

    // Would be great in AccountManager extension but can't add companion in extension for now
    companion object
    {
        const val KEY_AUTH_TOKEN_TYPE = "authTokenType"
        const val KEY_FEATURES = "features"
        const val DEFAULT_AUTH_TOKEN_TYPE = "defaultAuthTokenType"
    }
}
