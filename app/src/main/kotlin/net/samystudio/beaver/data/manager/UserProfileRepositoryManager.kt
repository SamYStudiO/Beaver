package net.samystudio.beaver.data.manager

import io.reactivex.rxjava3.core.Observable
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.User
import net.samystudio.beaver.data.remote.UserProfileApiInterface
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.main.userProfile.UserProfileFragmentModule
import javax.inject.Inject

/**
 * @see UserProfileFragmentModule.provideUserProfileApiInterface
 */
@FragmentScope
class UserProfileRepositoryManager @Inject constructor(private val userProfileApiInterface: UserProfileApiInterface) {
    fun userProfile(): Observable<ResultAsyncState<User>> =
        userProfileApiInterface.userProfile().toResultAsyncState()
}