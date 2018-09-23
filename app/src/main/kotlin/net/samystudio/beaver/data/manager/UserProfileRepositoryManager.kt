package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.DataAsyncState
import net.samystudio.beaver.data.model.UserProfile
import net.samystudio.beaver.data.remote.api.UserProfileApiInterface
import net.samystudio.beaver.di.scope.FragmentScope
import javax.inject.Inject

/**
 * @see [net.samystudio.beaver.ui.main.userProfile.UserProfileFragmentModule.provideUserProfileApiInterface]
 */
@FragmentScope
class UserProfileRepositoryManager @Inject constructor(private val userProfileApiInterface: UserProfileApiInterface) {
    fun userProfile(): Observable<DataAsyncState<UserProfile>> =
        userProfileApiInterface.userProfile()
            .toObservable()
            .map { DataAsyncState.Completed(it) as DataAsyncState<UserProfile> }
            .onErrorReturn { DataAsyncState.Error(it) }
            .startWith(DataAsyncState.Started())
}