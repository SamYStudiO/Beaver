package net.samystudio.beaver.data.remote

import net.samystudio.beaver.data.model.Home
import retrofit2.http.GET

interface HomeApiInterface {
    @GET("home.json")
    suspend fun home(): Home
}
