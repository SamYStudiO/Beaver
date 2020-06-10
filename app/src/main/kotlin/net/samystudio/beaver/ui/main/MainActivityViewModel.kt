package net.samystudio.beaver.ui.main

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.R
import net.samystudio.beaver.data.AsyncState
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

    override fun handleReady() {
        super.handleReady()

        // If we got authenticatorResponse that mean we were ask from system to create
        // account, so let's notify we want to authenticate.
        if (hasAuthenticatorResponse) navigate(NavigationRequest.Push(R.id.action_global_authenticator))

        disposables.add(
            googleApiAvailabilityManager.availabilityObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it is AsyncState.Failed
                        && it.error is GoogleApiAvailabilityManager.GoogleApiAvailabilityException
                        && !it.error.isResolvable
                    ) {
                        // TODO Notify app incompatible.
                    }
                }
        )
    }
}