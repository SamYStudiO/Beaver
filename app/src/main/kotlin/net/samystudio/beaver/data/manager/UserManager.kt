@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.data.manager

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import android.app.Activity
import android.content.Intent
import android.os.Build
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @see [net.samystudio.beaver.di.module.SystemServiceModule.provideAccountManager]
 */
@Singleton
class UserManager @Inject constructor(
    private val accountManager: AccountManager,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : OnAccountsUpdateListener {
    private var chooseAccountObservable: PublishSubject<Account> = PublishSubject.create()
    private val _statusObservable: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val statusObservable: Observable<Boolean> = _statusObservable
    val token: String?
        get() = sharedPreferencesHelper.accountToken ?: getCurrentAccount()?.let {
            accountManager.peekAuthToken(it, DEFAULT_AUTH_TOKEN_TYPE)
        }

    init {
        accountManager.addOnAccountsUpdatedListener(this, null, true)
    }

    override fun onAccountsUpdated(accounts: Array<out Account>?) {
        val account = getCurrentAccount()
        val connected = account != null && accountManager.peekAuthToken(
            account,
            DEFAULT_AUTH_TOKEN_TYPE
        ) != null
        if (_statusObservable.value != connected)
            _statusObservable.onNext(connected)
    }

    /**
     * Only meant to be called when sign up api responds successfully.
     */
    internal fun createAccount(email: String, password: String, token: String? = null) {
        if (token != null) connect(email, password, token)
        else if (accountManager.addAccountExplicitly(
                Account(email, ACCOUNT_TYPE),
                password,
                null
            )
        ) sharedPreferencesHelper.accountName = email
    }

    /**
     * Only meant to be called when sign in api responds successfully.
     */
    internal fun connect(email: String, password: String, token: String) {
        val account = getAccount(email)

        if (account == null)
            createAccount(email, password)

        getAccount(email)?.let {
            accountManager.setPassword(it, password)
            accountManager.setAuthToken(it, DEFAULT_AUTH_TOKEN_TYPE, token)
            sharedPreferencesHelper.accountName = email
            sharedPreferencesHelper.accountToken = token

            if (_statusObservable.value != true)
                _statusObservable.onNext(true)
        }
    }

    fun disconnect() {
        getCurrentAccount()?.let {
            accountManager.invalidateAuthToken(
                ACCOUNT_TYPE,
                accountManager.peekAuthToken(it, DEFAULT_AUTH_TOKEN_TYPE)
            )
            accountManager.clearPassword(it)
            sharedPreferencesHelper.accountToken = null

            if (_statusObservable.value != false)
                _statusObservable.onNext(false)
        }
    }

    fun isConnected() = !token.isNullOrBlank()

    private fun getAccount(email: String): Account? {
        accountManager.accounts.forEach {
            if (it.name == email) return it
        }

        return null
    }

    fun getCurrentAccount(activity: Activity): Single<Account> {
        val account = getCurrentAccount()

        return if (account != null) Single.just(account) else run {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.startActivityForResult(
                    AccountManager.newChooseAccountIntent(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    ), REQUEST_CODE_CHOOSE_ACCOUNT
                )
            } else {
                @Suppress("DEPRECATION")
                activity.startActivityForResult(
                    AccountManager.newChooseAccountIntent(
                        null,
                        null,
                        null,
                        true,
                        null,
                        null,
                        null,
                        null
                    ), REQUEST_CODE_CHOOSE_ACCOUNT
                )
            }

            chooseAccountObservable.singleOrError()
        }
    }

    fun getCurrentAccount(): Account? {
        val accountName = sharedPreferencesHelper.accountName ?: return null
        val accounts = accountManager.accounts

        accounts.forEach { account ->
            if (accountName == account.name)
                return account
        }

        sharedPreferencesHelper.accountName = null
        return null
    }

    fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            val account = if (name != null) getAccount(name) else null

            if (account != null) chooseAccountObservable.onNext(account)
        }
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