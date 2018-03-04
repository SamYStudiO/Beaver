package net.samystudio.beaver.data.remote

import io.reactivex.Single
import net.samystudio.beaver.data.model.Home
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApiInterface
{
    @GET("home.json")
    fun home(@Header("Authorization") authorization: String): Single<Home>
}