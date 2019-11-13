package net.samystudio.beaver.data.remote

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticatorApiInterface {
    @POST("signIn")
    @FormUrlEncoded
    fun signIn(@Field("email") email: String, @Field("password") password: String): Single<String>

    @POST("signUp")
    @FormUrlEncoded
    fun signUp(@Field("email") email: String, @Field("password") password: String): Single<String>
}