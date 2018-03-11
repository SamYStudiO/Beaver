package net.samystudio.beaver.ui.authenticator

import android.os.Bundle
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseSimpleFragment
import net.samystudio.beaver.ui.main.MainActivity

/**
 * Activity for sign in/up fragment(s)
 */
class AuthenticatorActivity : BaseActivity<AuthenticatorActivityViewModel>()
{
    override val defaultFragmentClass: Class<out BaseSimpleFragment>
        get() = AuthenticatorFragment::class.java
    override val defaultFragmentBundle: Bundle?
        get() = null
    override val layoutViewRes: Int
        get() = R.layout.activity_authenticator
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

        // If we come for example from Android "Users & accounts" settings we may have no
        // MainActivity running so we may need to start it.
        if (isTaskRoot) fragmentNavigationManager.startActivity(MainActivity::class.java)

        super.finish()
    }
}
