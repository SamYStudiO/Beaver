package net.samystudio.beaver.ui.main

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import net.samystudio.beaver.R
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel @Inject constructor() : BaseActivityViewModel() {
    private var hasAuthenticatorResponse: Boolean = false

    override fun handleIntent(intent: Intent) {
        super.handleIntent(intent)

        hasAuthenticatorResponse = intent.getParcelableExtra<AccountAuthenticatorResponse>(
            AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE
        ) != null
    }

    override fun handleReady() {
        super.handleReady()

        // If we got authenticatorResponse that mean we were ask from system to create
        // account, so let's notify we want to authenticate.
        if (hasAuthenticatorResponse) navigate(NavigationRequest.Push(R.id.action_global_authenticator))
    }
}