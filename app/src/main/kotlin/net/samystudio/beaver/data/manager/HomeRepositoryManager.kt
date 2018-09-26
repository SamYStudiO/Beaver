package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.ResultAsyncState
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.api.HomeApiInterface
import net.samystudio.beaver.di.scope.FragmentScope
import javax.inject.Inject

/**
 * @see [net.samystudio.beaver.ui.main.home.HomeFragmentModule.provideHomeApiInterface]
 */
@FragmentScope
class HomeRepositoryManager @Inject constructor(private val homeApiInterface: HomeApiInterface) {
    fun home(): Observable<ResultAsyncState<Home>> =
        homeApiInterface.home()
            .toObservable()
            .map { ResultAsyncState.Completed(it) as ResultAsyncState<Home> }
            .onErrorReturn { ResultAsyncState.Failed(it) }
            .startWith(ResultAsyncState.Started())
}