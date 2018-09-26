package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.UserProfile
import net.samystudio.beaver.data.remote.api.UserProfileApiInterface
import net.samystudio.beaver.di.scope.FragmentScope
import javax.inject.Inject

/**
 * @see [net.samystudio.beaver.ui.main.userProfile.UserProfileFragmentModule.provideUserProfileApiInterface]
 */
@FragmentScope
class UserProfileRepositoryManager @Inject constructor(private val userProfileApiInterface: UserProfileApiInterface) {
    fun userProfile(): Observable<ResultAsyncState<UserProfile>> =
        userProfileApiInterface.userProfile()
            .toObservable()
            .map { ResultAsyncState.Completed(it) as ResultAsyncState<UserProfile> }
            .onErrorReturn { ResultAsyncState.Failed(it) }
            .startWith(ResultAsyncState.Started())
}