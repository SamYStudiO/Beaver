package net.samystudio.beaver.data.remote

import io.reactivex.Single
import net.samystudio.beaver.data.model.User
import retrofit2.http.GET

interface UserProfileApiInterface {
    @GET("user_profile.json")
    fun userProfile(): Single<User>
}