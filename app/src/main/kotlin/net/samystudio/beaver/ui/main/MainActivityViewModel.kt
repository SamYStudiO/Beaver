package net.samystudio.beaver.ui.main

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import net.samystudio.beaver.R
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.GoogleApiAvailabilityManager
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.NavigationRequest

class MainActivityViewModel @ViewModelInject constructor(
    private val googleApiAvailabilityManager: GoogleApiAvailabilityManager,
    private val userManager: UserManager
) :
    BaseActivityViewModel() {
    private var hasAuthenticatorResponse: Boolean = false

    override fun handleCreate(savedInstanceState: Bundle?) {
        super.handleCreate(savedInstanceState)

        disposables.add(userManager.statusObservable.toFlowable(BackpressureStrategy.LATEST)
            .subscribe {
                if (it) handleUserConnected()
                else handleUserDisconnected()
            })
    }

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

    private fun handleUserConnected() {}

    private fun handleUserDisconnected() {
        navigate(NavigationRequest.Push(R.id.action_global_authenticator))
    }
}