package net.samystudio.beaver.ui.authenticator

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.main.MainActivity

class AuthenticatorActivity : BaseActivity<AuthenticatorActivityViewModel>()
{
    override val defaultFragmentClass: Class<out BaseFragment<*>>
        get() = AuthenticatorFragment::class.java
    override val defaultFragmentBundle: Bundle?
        get() = null
    override val layoutViewRes: Int
        get() = R.layout.activity_account_authenticator
    override val viewModelClass: Class<AuthenticatorActivityViewModel>
        get() = AuthenticatorActivityViewModel::class.java

    override fun onResume()
    {
        super.onResume()

        onBackStackChanged()
    }

    override fun finish()
    {
        viewModel.handleFinish()

        if (isTaskRoot) fragmentNavigationManager.startActivity(MainActivity::class.java)

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
