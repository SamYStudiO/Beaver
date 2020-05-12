package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.HomeApiInterface
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.main.home.HomeFragmentModule
import javax.inject.Inject

/**
 * @see HomeFragmentModule.provideHomeApiInterface
 */
@FragmentScope
class HomeRepositoryManager @Inject constructor(private val homeApiInterface: HomeApiInterface) {
    fun home(): Observable<ResultAsyncState<Home>> =
        homeApiInterface.home().toResultAsyncState()
}