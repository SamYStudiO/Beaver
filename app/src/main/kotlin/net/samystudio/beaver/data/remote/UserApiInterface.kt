package net.samystudio.beaver.data.remote

import net.samystudio.beaver.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApiInterface {
    @GET("user_profile.json?raw=true")
    @RequireAuthorization
    suspend fun getUser(): User

    @PATCH("user_profile.json?raw=true")
    @RequireAuthorization
    suspend fun patchUser(@Body user: User)
}
