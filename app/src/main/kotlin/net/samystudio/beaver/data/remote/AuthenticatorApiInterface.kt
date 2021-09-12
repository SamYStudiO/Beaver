package net.samystudio.beaver.data.remote

import net.samystudio.beaver.data.model.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticatorApiInterface {
    @POST("signIn")
    @FormUrlEncoded
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Token

    @POST("signUp")
    @FormUrlEncoded
    suspend fun signUp(
        @Field("email") email: String,
        @Field("password") password: String
    ): Token

    @POST("refreshToken")
    @FormUrlEncoded
    suspend fun refreshToken(@Field("token") token: String): Token
}
