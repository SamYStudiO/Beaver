package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.DataAsyncState
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.api.HomeApiInterface
import net.samystudio.beaver.di.scope.FragmentScope
import javax.inject.Inject

/**
 * @see [net.samystudio.beaver.ui.main.home.HomeFragmentModule.provideHomeApiInterface]
 */
@FragmentScope
class HomeRepositoryManager @Inject constructor(private val homeApiInterface: HomeApiInterface) {
    fun home(): Observable<DataAsyncState<Home>> =
        homeApiInterface.home()
            .toObservable()
            .map { DataAsyncState.Completed(it) as DataAsyncState<Home> }
            .onErrorReturn {
                DataAsyncState.Error(it)
            }
            .startWith(DataAsyncState.Started())
}