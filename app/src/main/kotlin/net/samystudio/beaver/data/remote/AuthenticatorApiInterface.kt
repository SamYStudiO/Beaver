package net.samystudio.beaver.data.remote

import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.model.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface AuthenticatorApiInterface {
    @POST("signIn")
    @FormUrlEncoded
    fun signIn(@Field("email") email: String, @Field("password") password: String): Single<Token>

    @POST("signUp")
    @FormUrlEncoded
    fun signUp(@Field("email") email: String, @Field("password") password: String): Single<Token>

    @POST("refreshToken")
    @FormUrlEncoded
    fun refreshToken(@Field("token") token: String): Single<Token>
}

@Singleton
class AuthenticatorApiInterfaceImpl @Inject constructor(
    private val authenticatorApiInterface: AuthenticatorApiInterface
) {
    fun signIn(email: String, password: String): Single<Token> =
        authenticatorApiInterface
            .signIn(email, password)
            .onErrorResumeNext {
                if (BuildConfig.DEBUG)
                    Single.just(Token.DEBUG)
                else Single.error(it)
            }

    fun signUp(email: String, password: String): Single<Token> =
        authenticatorApiInterface
            .signUp(email, password)
            .onErrorResumeNext {
                if (BuildConfig.DEBUG)
                    Single.just(Token.DEBUG)
                else Single.error(it)
            }

    fun refreshToken(token: String): Single<Token> =
        authenticatorApiInterface
            .refreshToken(token)
            .onErrorResumeNext {
                if (BuildConfig.DEBUG && token == Token.DEBUG.refreshToken)
                    Single.just(Token.DEBUG)
                else Single.error(it)
            }
}
