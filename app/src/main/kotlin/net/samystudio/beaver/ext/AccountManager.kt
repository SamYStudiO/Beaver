package net.samystudio.beaver.ext

import android.accounts.Account
import android.accounts.AccountManager
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.ui.authenticator.AuthenticatorActivity

fun AccountManager.getCurrentAccount(type: String, name: String?): Account?
{
    val accounts: Array<Account> =
        getAccountsByType(type)

    if (name != null)
    {
        accounts.forEach { account ->
            if (name == account.name)
                return account
        }
    }

    return if (accounts.isEmpty()) null else accounts[0]
}

fun AccountManager.invalidateCurrentAuthToken(accountName: String?)
{
    invalidateAuthToken(BuildConfig.APPLICATION_ID,
                        peekAuthToken(
                            getCurrentAccount(BuildConfig.APPLICATION_ID, accountName),
                            AuthenticatorActivity.DEFAULT_AUTH_TOKEN_TYPE))
}
