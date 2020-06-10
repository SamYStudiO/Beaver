package net.samystudio.beaver.data.remote

import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.model.Home
import retrofit2.http.GET

interface HomeApiInterface {
    @GET("home.json")
    fun home(): Single<Home>
}