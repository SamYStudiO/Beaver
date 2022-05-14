package net.samystudio.beaver.data.remote

import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.model.Token
import net.samystudio.beaver.util.EMAIL_VALIDATOR
import net.samystudio.beaver.util.PASSWORD_MAX_LENGTH
import net.samystudio.beaver.util.PASSWORD_MIN_LENGTH
import net.samystudio.beaver.util.PASSWORD_VALIDATOR
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface AuthenticatorApiInterface {
    @POST("login")
    @FormUrlEncoded
    fun login(@Field("email") email: String, @Field("password") password: String): Single<Token>

    @POST("register")
    @FormUrlEncoded
    fun register(@Field("email") email: String, @Field("password") password: String): Single<Token>

    @POST("refreshToken")
    @FormUrlEncoded
    fun refreshToken(@Field("refreshToken") refreshToken: String): Single<Token>
}

@Singleton
class AuthenticationApiInterfaceImpl @Inject constructor(
    private val authenticationApiInterface: AuthenticatorApiInterface
) {
    fun login(email: String, password: String): Single<Token> =
        when {
            email.isBlank() || password.isBlank() ->
                Single.error(MissingCredentialsException())
            else ->
                authenticationApiInterface.login(email, password)
                    .onErrorResumeNext {
                        if (BuildConfig.DEBUG)
                            Single.just(Token.DEBUG)
                        else
                            Single.error(it)
                    }
        }

    fun register(email: String, password: String): Single<Token> =
        when {
            email.isBlank() || password.isBlank() ->
                Single.error(MissingCredentialsException())
            !EMAIL_VALIDATOR.validate(email) ->
                Single.error(EmailException())
            !PASSWORD_VALIDATOR.validate(password) ->
                Single.error(PasswordException())
            else ->
                authenticationApiInterface.register(email, password)
                    .onErrorResumeNext {
                        if (BuildConfig.DEBUG)
                            Single.just(Token.DEBUG)
                        else
                            Single.error(it)
                    }
        }

    fun refreshToken(token: Token): Single<Token> =
        authenticationApiInterface
            .refreshToken("${token.tokenType} ${token.refreshToken}")
            .onErrorResumeNext {
                if (BuildConfig.DEBUG && token == Token.DEBUG)
                    Single.just(Token.DEBUG)
                else
                    Single.error(it)
            }
}

class EmailException : Exception("Email is invalid")
class PasswordException :
    Exception("Password must be at least $PASSWORD_MIN_LENGTH and at most  $PASSWORD_MAX_LENGTH")

class PasswordMatchException : Exception("Password must match confirmation")
class MissingCredentialsException : Exception("No username or password provided")
