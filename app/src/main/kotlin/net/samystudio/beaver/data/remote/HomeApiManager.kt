package net.samystudio.beaver.data.remote

import android.accounts.AccountManager
import io.reactivex.Single
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesManager
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.getCurrentAccount
import net.samystudio.beaver.ui.authenticator.AuthenticatorActivity
import javax.inject.Inject

@FragmentScope
class HomeApiManager
@Inject constructor(private val homeApiInterface: HomeApiInterface,
                    private val accountManager: AccountManager,
                    private val sharedPreferencesManager: SharedPreferencesManager)
{
    fun home(): Single<Home>
    {
        val account = accountManager.getCurrentAccount(BuildConfig.APPLICATION_ID,
                                                       sharedPreferencesManager.accountName)
                ?: return Single.error(AuthorizationException())

        return homeApiInterface.home(accountManager.peekAuthToken(account,
                                                                  AuthenticatorActivity.DEFAULT_AUTH_TOKEN_TYPE))
    }
}