package net.samystudio.beaver.data.manager

import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.User
import net.samystudio.beaver.data.remote.UserProfileApiInterface
import net.samystudio.beaver.data.toResultAsyncState
import javax.inject.Inject

@ActivityScoped
class UserProfileRepositoryManager @Inject constructor(private val userProfileApiInterface: UserProfileApiInterface) {
    fun userProfile(): Observable<ResultAsyncState<User>> =
        userProfileApiInterface.userProfile().toResultAsyncState()
}