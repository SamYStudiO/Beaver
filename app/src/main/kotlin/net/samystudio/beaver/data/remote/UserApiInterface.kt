package net.samystudio.beaver.data.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.model.User
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface UserApiInterface {
    @GET("user_profile.json")
    @RequireAuthorization
    fun getUser(): Single<User>

    @POST("logout")
    fun logout(): Completable
}

@Singleton
class UserApiInterfaceImpl @Inject constructor(
    private val userApiInterface: UserApiInterface
) {
    fun getUser(): Single<User> = userApiInterface.getUser()

    fun logout(): Completable = userApiInterface.logout()
}
