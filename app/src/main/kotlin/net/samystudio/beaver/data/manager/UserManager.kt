package net.samystudio.beaver.data.manager

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.remote.api.AuthenticatorApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager
@Inject
constructor(private val accountManager: AccountManager,
            private val sharedPreferencesHelper: SharedPreferencesHelper,
            private val authenticatorApiInterface: AuthenticatorApiInterface) :
    OnAccountsUpdateListener
{
    private val _statusObservable: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val statusObservable: Observable<Boolean> = _statusObservable
    val token: String?
        get() = accountManager.peekAuthToken(getCurrentAccount(), DEFAULT_AUTH_TOKEN_TYPE)

    init
    {
        accountManager.addOnAccountsUpdatedListener(this, null, true)
    }

    override fun onAccountsUpdated(accounts: Array<out Account>?)
    {
        val account = getCurrentAccount()
        val connect = account != null && accountManager.peekAuthToken(account,
                                                                      DEFAULT_AUTH_TOKEN_TYPE) != null
        if (_statusObservable.value != connect)
            _statusObservable.onNext(connect)
    }

    fun signIn(email: String, password: String): Single<String>
    {
        return authenticatorApiInterface.signIn(email, password)
            .doOnSuccess({ token -> onNewToken(email, password, token) })
            .doOnError({ onNewToken(email, password, "toctoc") })
            .onErrorReturnItem("toctoc")
    }

    fun signUp(email: String, password: String): Single<String>
    {
        return authenticatorApiInterface.signUp(email, password)
            .doOnSuccess({ token -> onNewToken(email, password, token) })
    }

    fun invalidate()
    {
        accountManager.invalidateAuthToken(BuildConfig.APPLICATION_ID,
                                           accountManager.peekAuthToken(getCurrentAccount(),
                                                                        DEFAULT_AUTH_TOKEN_TYPE))
    }

    private fun getCurrentAccount(): Account?
    {
        val accountName = sharedPreferencesHelper.accountName
        val accounts = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID)

        if (accountName != null)
        {
            accounts.forEach { account ->
                if (accountName == account.name)
                    return account
            }
        }

        return if (accounts.isEmpty()) null else accounts[0]
    }

    private fun onNewToken(email: String, password: String, token: String)
    {
        var account =
            getCurrentAccount()

        if (account == null)
        {
            account = Account(email,
                              BuildConfig.APPLICATION_ID)

            val b = accountManager.addAccountExplicitly(account, password, null)
            if (b) sharedPreferencesHelper.accountName = account.name
        }

        accountManager.setAuthToken(account,
                                    DEFAULT_AUTH_TOKEN_TYPE,
                                    token)
    }

    companion object
    {
        const val KEY_AUTH_TOKEN_TYPE = "authTokenType"
        const val KEY_FEATURES = "features"
        const val DEFAULT_AUTH_TOKEN_TYPE = "defaultAuthTokenType"
    }
}