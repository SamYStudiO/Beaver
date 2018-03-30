package net.samystudio.beaver.data.remote.api

import io.reactivex.Single
import net.samystudio.beaver.data.model.Home
import retrofit2.http.GET

interface HomeApiInterface {
    @GET("home.json")
    fun home(): Single<Home>
}