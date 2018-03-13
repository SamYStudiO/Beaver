package net.samystudio.beaver.ui.main

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.viewmodel.TriggerCommand
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel
@Inject
constructor() : BaseActivityViewModel()
{
    private var authenticatorResponse: AccountAuthenticatorResponse? = null
    val requestAuthenticatorObservable: TriggerCommand = TriggerCommand()

    override fun handleState(intent: Intent,
                             savedInstanceState: Bundle?,
                             resultRequestCode: Int?,
                             resultCode: Int?,
                             resultData: Intent?)
    {
        super.handleState(intent, savedInstanceState, resultRequestCode, resultCode, resultData)

        if (authenticatorResponse == null)
        {
            // Android system send use an AccountAuthenticatorResponse that we need to fill, so the
            // account may be created from the system.
            authenticatorResponse =
                    intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)

            authenticatorResponse?.onRequestContinued()
        }

        authenticatorResponse?.let {
            if (resultRequestCode == AUTHENTICATOR_REQUEST_CODE)
            {
                if (resultCode == Activity.RESULT_OK)
                    it.onResult(resultData?.extras)
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
        if (authenticatorResponse != null) requestAuthenticator()
    }

    fun requestAuthenticator()
    {
        requestAuthenticatorObservable.call()
    }

    companion object
    {
        const val AUTHENTICATOR_REQUEST_CODE = 147
    }
}