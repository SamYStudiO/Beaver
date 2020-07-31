@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.data.manager

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val accountManager: AccountManager,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : OnAccountsUpdateListener {
    private val _statusObservable: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val statusObservable: Observable<Boolean> = _statusObservable
    val token: String?
        get() {
            val token = sharedPreferencesHelper.accountToken.get()
            return if (token.isNotBlank()) token
            else getCurrentAccount()?.let {
                accountManager.peekAuthToken(
                    it,
                    DEFAULT_AUTH_TOKEN_TYPE
                )
            }
        }

    init {
        accountManager.addOnAccountsUpdatedListener(this, null, true)
    }

    override fun onAccountsUpdated(accounts: Array<out Account>?) {
        val connected = isConnected()
        if (_statusObservable.value != connected)
            _statusObservable.onNext(connected)
    }

    fun disconnect() {
        getCurrentAccount()?.let {
            accountManager.invalidateAuthToken(
                ACCOUNT_TYPE,
                accountManager.peekAuthToken(it, DEFAULT_AUTH_TOKEN_TYPE)
            )
            accountManager.clearPassword(it)
            sharedPreferencesHelper.accountToken.delete()

            if (_statusObservable.value != false)
                _statusObservable.onNext(false)
        }
    }

    fun isConnected() = !token.isNullOrBlank()

    /**
     * Only meant to be called when sign up api responds successfully.
     */
    /*private*/internal fun createAccount(email: String, password: String, token: String? = null) {
        if (token != null) connect(email, password, token)
        else if (accountManager.addAccountExplicitly(
                Account(email, ACCOUNT_TYPE),
                password,
                null
            )
        ) sharedPreferencesHelper.accountName.set(email)
    }

    /**
     * Only meant to be called when sign in api responds successfully.
     */
    /*private*/internal fun connect(email: String, password: String, token: String) {
        val account = getAccount(email)

        if (account == null)
            createAccount(email, password)

        getAccount(email)?.let {
            accountManager.setPassword(it, password)
            accountManager.setAuthToken(it, DEFAULT_AUTH_TOKEN_TYPE, token)
            sharedPreferencesHelper.accountName.set(email)
            sharedPreferencesHelper.accountToken.set(token)

            if (_statusObservable.value != true)
                _statusObservable.onNext(true)
        }
    }

    private fun getAccount(email: String): Account? {
        accountManager.accounts.forEach {
            if (it.name == email) return it
        }

        return null
    }

    private fun getCurrentAccount(): Account? {
        val accountName = sharedPreferencesHelper.accountName.get()

        if (accountName.isBlank()) return null

        val accounts = accountManager.accounts
        accounts.forEach { account ->
            if (accountName == account.name)
                return account
        }

        sharedPreferencesHelper.accountName.delete()
        return null
    }

    companion object {
        const val KEY_AUTH_TOKEN_TYPE = "authTokenType"
        const val KEY_FEATURES = "features"
        const val KEY_CREATE_ACCOUNT = "createAccount"
        const val KEY_CONFIRM_ACCOUNT = "confirmAccount"
        const val DEFAULT_AUTH_TOKEN_TYPE = "defaultAuthTokenType"
        const val ACCOUNT_TYPE = BuildConfig.APPLICATION_ID
        const val ACCOUNT_TYPE_GOOGLE = "com.google"
        const val ACCOUNT_TYPE_FACEBOOK = "com.facebook.auth.login"
        const val ACCOUNT_TYPE_TWITTER = "com.twitter.android.oauth.token"
        const val REQUEST_CODE_CHOOSE_ACCOUNT = 354
    }
}