package net.samystudio.beaver.data.remote

import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.model.TOKEN_DEBUG
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
    suspend fun login(@Field("email") email: String, @Field("password") password: String): Token

    @POST("register")
    @FormUrlEncoded
    suspend fun register(@Field("email") email: String, @Field("password") password: String): Token

    @POST("refreshToken")
    @FormUrlEncoded
    suspend fun refreshToken(@Field("refreshToken") refreshToken: String): Token
}

@Singleton
class AuthenticationApiInterfaceImpl @Inject constructor(
    private val authenticationApiInterface: AuthenticatorApiInterface
) {
    suspend fun login(email: String, password: String): Token =
        when {
            email.isBlank() || password.isBlank() ->
                throw MissingCredentialsException()
            else ->
                try {
                    authenticationApiInterface.login(email, password)
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG)
                        TOKEN_DEBUG
                    else
                        throw e
                }
        }

    suspend fun register(email: String, password: String): Token =
        when {
            email.isBlank() || password.isBlank() ->
                throw MissingCredentialsException()
            !EMAIL_VALIDATOR.validate(email) ->
                throw EmailException()
            !PASSWORD_VALIDATOR.validate(password) ->
                throw PasswordException()
            else ->
                try {
                    authenticationApiInterface.register(email, password)
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG)
                        TOKEN_DEBUG
                    else
                        throw e
                }
        }

    suspend fun refreshToken(token: Token): Token =
        try {
            authenticationApiInterface
                .refreshToken("${token.tokenType} ${token.refreshToken}")
        } catch (e: Exception) {
            if (BuildConfig.DEBUG && token == TOKEN_DEBUG)
                TOKEN_DEBUG
            else
                throw e
        }
}

class EmailException : Exception("Email is invalid")
class PasswordException :
    Exception("Password must be at least $PASSWORD_MIN_LENGTH and at most  $PASSWORD_MAX_LENGTH")

class PasswordMatchException : Exception("Password must match confirmation")
class MissingCredentialsException : Exception("No username or password provided")
