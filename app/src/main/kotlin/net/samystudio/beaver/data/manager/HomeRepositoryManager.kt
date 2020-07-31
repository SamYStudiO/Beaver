package net.samystudio.beaver.data.manager

import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.HomeApiInterface
import net.samystudio.beaver.data.toResultAsyncState
import javax.inject.Inject

@ActivityScoped
class HomeRepositoryManager @Inject constructor(private val homeApiInterface: HomeApiInterface) {
    fun home(): Observable<ResultAsyncState<Home>> =
        homeApiInterface.home().toResultAsyncState()
}