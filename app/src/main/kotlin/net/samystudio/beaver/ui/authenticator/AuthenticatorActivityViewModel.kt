package net.samystudio.beaver.ui.authenticator

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import javax.inject.Inject

@ActivityScope
class AuthenticatorActivityViewModel
@Inject
constructor() : BaseActivityViewModel()
{
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

    fun setAuthenticatorResult(result: Bundle)
    {
        resultBundle = result
    }

    fun handleFinish()
    {
        authenticatorResponse?.let {

            if (resultBundle != null)
                it.onResult(resultBundle)
            else
                it.onError(AccountManager.ERROR_CODE_CANCELED, "canceled")

            authenticatorResponse = null
        }
    }
}