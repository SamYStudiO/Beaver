package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.data.remote.api.HomeApiInterface
import net.samystudio.beaver.di.scope.FragmentScope
import javax.inject.Inject

/**
 * @see [net.samystudio.beaver.ui.main.home.HomeFragmentModule.provideHomeApiInterface]
 */
@FragmentScope
class HomeRepositoryManager @Inject constructor(private val homeApiInterface: HomeApiInterface) {
    fun home(): Observable<DataRequestState<Home>> =
        homeApiInterface.home()
            .toObservable()
            .map { DataRequestState.Success(it) as DataRequestState<Home> }
            .onErrorReturn {
                DataRequestState.Error(it)
            }
            .startWith(DataRequestState.Start())
}