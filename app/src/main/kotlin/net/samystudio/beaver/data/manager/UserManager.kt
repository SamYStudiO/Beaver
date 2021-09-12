@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.data.manager

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.model.TOKEN_DEBUG
import net.samystudio.beaver.data.model.Token
import net.samystudio.beaver.data.remote.AuthenticatorApiInterface
import net.samystudio.beaver.data.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val authenticationApiInterface: AuthenticatorApiInterface,
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager,
) {
    @ExperimentalCoroutinesApi
    @DelicateCoroutinesApi
    val userFlow = userRepository.userFlow
    val userConnectedFlow
        get() = preferencesManager.account().map {
            it.hasAccountToken()
        }

    suspend fun signIn(email: String, password: String) {
        if (BuildConfig.DEBUG)
            writeToken(TOKEN_DEBUG)
        else
            writeToken(authenticationApiInterface.signIn(email, password))
    }

    suspend fun signUp(email: String, password: String) {
        if (BuildConfig.DEBUG)
            writeToken(TOKEN_DEBUG)
        else
            writeToken(authenticationApiInterface.signUp(email, password))
    }

    suspend fun refreshToken() {
        if (BuildConfig.DEBUG)
            writeToken(TOKEN_DEBUG)
        else
            try {
                preferencesManager.accountToken().map {
                    writeToken(authenticationApiInterface.refreshToken(it.refreshToken))
                }
            } catch (e: Throwable) {
                Timber.w(e, "An error occurred refreshing token")
                disconnect()
            }
    }

    suspend fun getUser() = userRepository.getUser()

    suspend fun disconnect() {
        preferencesManager.clearAccountToken()
    }

    private suspend fun writeToken(token: Token) {
        preferencesManager.updateAccountToken(token)
    }

    class TokenException : RuntimeException("No valid token available")
}
