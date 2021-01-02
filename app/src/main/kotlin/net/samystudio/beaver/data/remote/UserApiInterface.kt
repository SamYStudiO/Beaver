package net.samystudio.beaver.data.remote

import dagger.Lazy
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.model.User
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

interface UserApiInterface {
    @GET("user_profile.json")
    fun getUser(): Single<User>
}

@Singleton
class UserApiInterfaceImpl @Inject constructor(
    userManager: Lazy<UserManager>,
    private val userApiInterface: UserApiInterface
) : BaseUserTokenApiInterface(userManager) {
    fun getUser(): Single<User> =
        userApiInterface
            .getUser()
            .compose(onTokenInvalidSingleTransformer())
}
