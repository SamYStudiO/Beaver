package net.samystudio.beaver.ui.authenticator

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Application
import android.content.Intent
import android.os.Bundle
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import net.samystudio.beaver.ui.main.MainActivity
import javax.inject.Inject

@ActivityScope
class AuthenticatorActivityViewModel
@Inject
constructor(application: Application,
            fragmentNavigationManager: FragmentNavigationManager) :
    BaseActivityViewModel(application, fragmentNavigationManager)
{
    override val defaultFragmentClass: Class<out BaseFragment<*>>
        get() = AuthenticatorFragment::class.java
    override val defaultFragmentBundle: Bundle?
        get() = null
    private var authenticatorResponse: AccountAuthenticatorResponse? = null
    private var resultBundle: Bundle? = null

    override fun handleState(intent: Intent, savedInstanceState: Bundle?, arguments: Bundle?)
    {
        super.handleState(intent, savedInstanceState, arguments)

        if (authenticatorResponse == null)
        {
            authenticatorResponse =
                    intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)

            authenticatorResponse?.onRequestContinued()
        }
    }

    override fun handleReady()
    {
        super.handleReady()

        onBackStackChanged()
    }

    fun setAuthenticatorResult(result: Bundle)
    {
        resultBundle = result
    }

    fun handleFinish(isTaskRoot: Boolean)
    {
        authenticatorResponse?.let {

            if (resultBundle != null)
                it.onResult(resultBundle)
            else
                it.onError(AccountManager.ERROR_CODE_CANCELED, "canceled")

            authenticatorResponse = null
        }

        if (isTaskRoot) startActivity(MainActivity::class.java)
    }
}