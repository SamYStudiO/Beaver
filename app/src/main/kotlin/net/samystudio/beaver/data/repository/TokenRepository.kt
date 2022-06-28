package net.samystudio.beaver.data.repository

import android.content.SharedPreferences
import dagger.Lazy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import net.samystudio.beaver.data.manager.PreferencesManager
import net.samystudio.beaver.data.model.Token
import net.samystudio.beaver.data.model.isValid
import net.samystudio.beaver.data.remote.AuthenticationApiInterfaceImpl
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours

@Singleton
class TokenRepository @Inject constructor(
    private val userRepository: Lazy<UserRepository>,
    private val preferencesManager: PreferencesManager,
    private val authenticationApiInterfaceImpl: AuthenticationApiInterfaceImpl,
    sharedPreferences: SharedPreferences,
) : DataRepository<Token>(
    24.hours,
    SharedPreferencesDataRepositoryIntegrityHolder(sharedPreferences),
    SharedPreferencesDataRepositoryIntegrityHolder(sharedPreferences),
) {
    override suspend fun getRemoteData(): Token =
        try {
            checkNotNull(
                authenticationApiInterfaceImpl.refreshToken(data ?: refreshFromLocal())
                    .takeIf { it.isValid }
            )
        } catch (e: Exception) {
            // If it's a TokenException or a 401 let's signal this is a TokenException so app can
            // log out from MainActivity.
            if (e is TokenException || (e is HttpException && e.code() == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                // Can't get a valid token clear user.
                userRepository.get().clear()
                throw TokenException(
                    "Couldn't refresh token, it may be expired or revoked",
                    e
                )
            } else
                throw(e)
        }

    override suspend fun getLocalData(): Token? =
        preferencesManager.accountToken.firstOrNull().takeIf { it.isValid }

    override suspend fun setLocalData(data: Token?) {
        if (data == null || !data.isValid)
            preferencesManager.clearAccountToken()
        else
            preferencesManager.updateAccountToken(data)
    }

    override fun getMissingLocalDataException() = TokenException("Couldn't find any token")

    suspend fun login(login: String, password: String): Token =
        authenticationApiInterfaceImpl.login(login, password).apply {
            refresh(this)
        }

    suspend fun register(
        email: String,
        password: String,
    ): Token =
        authenticationApiInterfaceImpl.register(email, password).apply {
            refresh(this)
        }
}

class TokenException(message: String, cause: Throwable? = null) : Exception(message, cause)
