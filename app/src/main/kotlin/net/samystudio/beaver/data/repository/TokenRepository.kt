package net.samystudio.beaver.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import dagger.Lazy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleTransformer
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.model.Token
import net.samystudio.beaver.data.model.isValid
import net.samystudio.beaver.data.remote.AuthenticationApiInterfaceImpl
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Singleton
class TokenRepository @Inject constructor(
    private val userRepository: Lazy<UserRepository>,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val authenticationApiInterfaceImpl: AuthenticationApiInterfaceImpl,
    private val gson: Gson,
) : DataRepository<Token>(
    24.hours,
) {
    override fun getAge(): Duration =
        (System.currentTimeMillis() - sharedPreferencesHelper.tokenDate.get()).toDuration(
            DurationUnit.MILLISECONDS
        )

    override fun getRemoteDataSingle(): Single<Token> =
        (data?.let { Single.just(it) } ?: getLocalData())
            .flatMap { authenticationApiInterfaceImpl.refreshToken(it) }
            .onErrorResumeNext {
                // If it's a 401 let's signal this is a TokenException so app can log out from
                // MainActivity.
                if (it is HttpException && it.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    // Can't get a valid token clear user.
                    userRepository.get().clear()
                        .onErrorComplete()
                        .andThen(
                            Single.error(
                                TokenException(
                                    "Couldn't refresh token, it may be expired or revoked",
                                    it
                                )
                            )
                        )
                } else
                    Single.error(it)
            }

    override fun getLocalDataSingle(): Single<Token> = Single.fromCallable {
        val token = try {
            gson.fromJson(sharedPreferencesHelper.token.get(), Token::class.java)
        } catch (e: JsonParseException) {
            throw TokenException("Error parsing local token json", e)
        } catch (e: JsonSyntaxException) {
            throw TokenException("Error parsing local token json", e)
        }

        if (token.isValid)
            token
        else
            throw TokenException("Local json is missing or invalid")
    }

    override fun setLocalDataSingle(data: Token?): Completable? = Completable.fromCallable {
        if (data == null) {
            sharedPreferencesHelper.token.delete()
            sharedPreferencesHelper.tokenDate.delete()
        } else {
            sharedPreferencesHelper.token.set(gson.toJson(data))
            sharedPreferencesHelper.tokenDate.set(System.currentTimeMillis())
        }
    }

    override fun getMissingLocalDataException() = TokenException("Couldn't find any token")

    fun login(login: String, password: String): Single<Token> =
        authenticationApiInterfaceImpl.login(login, password)
            .compose(tokenRefreshTransformer())

    fun register(
        email: String,
        password: String,
    ): Single<Token> =
        authenticationApiInterfaceImpl.register(
            email,
            password,
        ).compose(tokenRefreshTransformer())

    /**
     * Refresh [TokenRepository] manually.
     */
    private fun tokenRefreshTransformer() =
        SingleTransformer<Token, Token> { single ->
            single.flatMap { refresh(it) }
        }
}

class TokenException(message: String, cause: Throwable? = null) : Exception(message, cause)
