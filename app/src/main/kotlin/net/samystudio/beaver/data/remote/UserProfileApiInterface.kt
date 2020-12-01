package net.samystudio.beaver.data.remote

import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.User
import net.samystudio.beaver.data.toResultAsyncState
import retrofit2.http.GET
import javax.inject.Inject

interface UserProfileApiInterface {
    @GET("user_profile.json")
    fun userProfile(): Single<User>
}

@ActivityScoped
class UserProfileApiInterfaceImpl @Inject constructor(private val userProfileApiInterface: UserProfileApiInterface) {
    fun userProfile(): Observable<ResultAsyncState<User>> =
        userProfileApiInterface.userProfile().toResultAsyncState()
}