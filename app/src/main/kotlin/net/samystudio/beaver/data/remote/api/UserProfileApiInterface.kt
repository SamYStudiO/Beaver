package net.samystudio.beaver.data.remote.api

import io.reactivex.Single
import net.samystudio.beaver.data.model.UserProfile
import retrofit2.http.GET

interface UserProfileApiInterface {
    @GET("user_profile.json")
    fun userProfile(): Single<UserProfile>
}