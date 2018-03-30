package net.samystudio.beaver.data.manager

import android.accounts.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import net.samystudio.beaver.data.remote.api.AuthenticatorApiInterface
import net.samystudio.beaver.di.qualifier.ApplicationContext
import net.samystudio.beaver.ui.main.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorManager
@Inject
constructor(
    @param:ApplicationContext private val context: Context,
    private val accountManager: AccountManager,
    private val authenticatorInterface: AuthenticatorApiInterface
) :
    AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(authTokenType: String): String? {
        return null
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle?
    ): Bundle? {
        return null
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? {
        return null
    }

    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle? {
        var authToken = accountManager.peekAuthToken(account, authTokenType)

        if (authToken.isNullOrBlank()) {
            accountManager.getPassword(account)?.let {
                try {
                    authToken = authenticatorInterface.signIn(account.name, it).blockingGet()
                } catch (e: Throwable) {
                    throw NetworkErrorException(e)
                }
            }
        }

        val bundle = Bundle()

        if (!authToken.isNullOrBlank()) {
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return bundle
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type)
        intent.putExtra(AccountManager.KEY_USERDATA, options)
        intent.putExtra(UserManager.KEY_AUTH_TOKEN_TYPE, authTokenType)

        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

    override fun hasFeatures(
        response: AccountAuthenticatorResponse,
        account: Account,
        features: Array<out String>
    ): Bundle? {
        return null
    }

    override fun editProperties(
        response: AccountAuthenticatorResponse,
        accountType: String
    ): Bundle? {
        return null
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle
    ): Bundle? {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
        intent.putExtra(AccountManager.KEY_USERDATA, options)
        intent.putExtra(UserManager.KEY_AUTH_TOKEN_TYPE, authTokenType)
        intent.putExtra(UserManager.KEY_FEATURES, requiredFeatures)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }
}