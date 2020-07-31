package net.samystudio.beaver.data.manager

import android.accounts.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import dagger.hilt.android.qualifiers.ApplicationContext
import net.samystudio.beaver.data.remote.AuthenticatorApiInterface
import net.samystudio.beaver.ui.main.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountManager: AccountManager,
    private val authenticatorApiInterface: AuthenticatorApiInterface
) : AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(authTokenType: String): String? {
        return authTokenType
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle?
    ): Bundle? {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtras(
            bundleOf(
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE to response,
                AccountManager.KEY_ACCOUNT_NAME to account.name,
                AccountManager.KEY_ACCOUNT_TYPE to account.type,
                AccountManager.KEY_USERDATA to options,
                UserManager.KEY_CONFIRM_ACCOUNT to true
            )
        )

        return bundleOf(AccountManager.KEY_INTENT to intent)
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
                    authToken = authenticatorApiInterface.signIn(account.name, it).blockingGet()
                } catch (e: Throwable) {
                    throw NetworkErrorException(e)
                }
            }
        }

        if (!authToken.isNullOrBlank()) {
            return bundleOf(
                AccountManager.KEY_ACCOUNT_NAME to account.name,
                AccountManager.KEY_ACCOUNT_TYPE to account.type,
                AccountManager.KEY_AUTHTOKEN to authToken
            )
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtras(
            bundleOf(
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE to response,
                AccountManager.KEY_ACCOUNT_NAME to account.name,
                AccountManager.KEY_ACCOUNT_TYPE to account.type,
                AccountManager.KEY_USERDATA to options,
                UserManager.KEY_AUTH_TOKEN_TYPE to authTokenType
            )
        )

        return bundleOf(AccountManager.KEY_INTENT to intent)
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
        intent.putExtras(
            bundleOf(
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE to response,
                AccountManager.KEY_ACCOUNT_TYPE to accountType,
                AccountManager.KEY_USERDATA to options,
                UserManager.KEY_AUTH_TOKEN_TYPE to authTokenType,
                UserManager.KEY_FEATURES to requiredFeatures,
                UserManager.KEY_CREATE_ACCOUNT to true
            )
        )

        return bundleOf(AccountManager.KEY_INTENT to intent)
    }
}