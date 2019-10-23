package net.samystudio.beaver.ui.main

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.R
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import javax.inject.Inject

@ActivityScope
class MainActivityViewModel @Inject constructor(private val googleApiAvailabilityManager: GoogleApiAvailabilityManager) :
    BaseActivityViewModel() {
    private var hasAuthenticatorResponse: Boolean = false

    override fun handleIntent(intent: Intent) {
        super.handleIntent(intent)

        hasAuthenticatorResponse = intent.getParcelableExtra<AccountAuthenticatorResponse>(
            AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE
        ) != null
    }

    override fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.handleResult(requestCode, resultCode, data)
        googleApiAvailabilityManager.onDialogResult(requestCode, resultCode)
    }

    override fun handleReady() {
        super.handleReady()

        // If we got authenticatorResponse that mean we were ask from system to create
        // account, so let's notify we want to authenticate.
        if (hasAuthenticatorResponse) navigate(NavigationRequest.Push(R.id.action_global_authenticator))

        disposables.add(
            googleApiAvailabilityManager.isAvailable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, {
                    // TODO notify app incompatible
                })
        )
    }
}