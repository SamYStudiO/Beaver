package net.samystudio.beaver.ui.authenticator

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.remote.AuthenticatorApiManager
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.getCurrentAccount
import net.samystudio.beaver.ext.invalidateCurrentAuthToken
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject

@FragmentScope
class AuthenticatorFragmentViewModel
@Inject
constructor(activityViewModel: AuthenticatorActivityViewModel,
            private val authenticatorApiManager: AuthenticatorApiManager) :
    BaseFragmentViewModel(activityViewModel)
{
    override val defaultTitle: String?
        get() = "account"

    fun signIn(email: String, password: String)
    {
        disposables.add(authenticatorApiManager.signIn(email, password)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ token ->
                                           handleSignResult(email, password, token)
                                       }, { throwable ->
                                           throwable.printStackTrace()
                                       }))
    }

    fun signUp(email: String, password: String)
    {
        disposables.add(authenticatorApiManager.signUp(email, password)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ token ->
                                           handleSignResult(email, password, token)
                                       }, { throwable ->
                                           throwable.printStackTrace()
                                       }))
    }

    fun invalidateToken()
    {
        accountManager.invalidateCurrentAuthToken(sharedPreferencesManager.accountName)
    }

    private fun handleSignResult(email: String, password: String, token: String)
    {
        var account =
            accountManager.getCurrentAccount(BuildConfig.APPLICATION_ID,
                                             sharedPreferencesManager.accountName)

        if (account == null)
        {
            account = Account(email,
                              BuildConfig.APPLICATION_ID)
            addAccount(account,
                       password,
                       null)
        }

        accountManager.setAuthToken(account,
                                    AuthenticatorActivity.DEFAULT_AUTH_TOKEN_TYPE,
                                    token)

        val bundle = Bundle()
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, email)
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE,
                         BuildConfig.APPLICATION_ID)
        bundle.putString(AccountManager.KEY_PASSWORD, password)

        (activityViewModel as AuthenticatorActivityViewModel)
            .setAuthenticatorResult(bundle)

        // just to close activity
        activityViewModel.setResult(Activity.RESULT_OK, null)
    }

    private fun addAccount(account: Account, password: String, userData: Bundle?): Boolean
    {
        val b = accountManager.addAccountExplicitly(account, password, userData)

        if (b) sharedPreferencesManager.accountName = account.name

        return b
    }
}