package net.samystudio.beaver.ui.main

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorController
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel
@Inject
constructor() : BaseActivityViewModel()
{
    private var authenticatorResponse: AccountAuthenticatorResponse? = null

    override fun handleIntent(intent: Intent)
    {
        super.handleIntent(intent)

        if (authenticatorResponse == null)
        {
            // Android system send use an AccountAuthenticatorResponse that we need to fill, so the
            // account may be created from the system.
            authenticatorResponse =
                    intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)

            authenticatorResponse?.onRequestContinued()
        }
    }

    override fun handleResult(requestCode: Int, code: Int, data: Intent?)
    {
        super.handleResult(requestCode, code, data)

        authenticatorResponse?.let {
            if (requestCode == AUTHENTICATOR_REQUEST_CODE)
            {
                if (code == Activity.RESULT_OK)
                    it.onResult(data?.extras)
                else
                    it.onError(AccountManager.ERROR_CODE_CANCELED, "canceled")

                authenticatorResponse = null
            }
        }
    }

    override fun handleReady()
    {
        super.handleReady()

        // If we got a non null authenticatorResponse that mean we were ask from system to create
        // account, so let's notify we want to authenticate.
        if (authenticatorResponse != null) navigate(
            NavigationRequest.Dialog(AuthenticatorController()))
    }

    companion object
    {
        const val AUTHENTICATOR_REQUEST_CODE = 147
    }
}