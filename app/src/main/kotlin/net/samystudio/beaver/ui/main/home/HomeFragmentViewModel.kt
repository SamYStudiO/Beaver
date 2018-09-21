package net.samystudio.beaver.ui.main.home

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Observable
import net.samystudio.beaver.R
import net.samystudio.beaver.data.DataAsyncState
import net.samystudio.beaver.data.manager.HomeRepositoryManager
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import net.samystudio.beaver.ui.common.viewmodel.DataRequestLiveData
import javax.inject.Inject

@FragmentScope
class HomeFragmentViewModel @Inject constructor(homeRepositoryManager: HomeRepositoryManager) :
    BaseFragmentViewModel(),
    DataFetchViewModel<Home> {
    private val _dataFetchObservable: DataRequestLiveData<Home> =
        DataRequestLiveData(homeRepositoryManager.home())
    override val dataFetchObservable: LiveData<DataAsyncState<Home>> = _dataFetchObservable

    override fun handleCreate() {
        super.handleCreate()
        disposables.add(_dataFetchObservable)
    }

    fun <T : HomeUserFlow> addUserFlow(observable: Observable<T>) {
        disposables.add(observable.flatMap { userFlow ->
            when (userFlow) {
                is HomeUserFlow.Disconnect ->
                    Completable.complete()
                        .doOnComplete { userManager.invalidateToken() }
                        .toObservable()
                else ->
                    Observable.just(Observable.error<Unit> {
                        IllegalArgumentException("Unknown user flow ${userFlow.getClassTag()}.")
                    })
            }
        }.subscribe())
    }

    override fun handleUserConnected() {
        super.handleUserConnected()
        refreshData()
    }

    override fun handleUserDisconnected() {
        super.handleUserDisconnected()
        navigate(NavigationRequest.Push(R.id.action_home_to_authenticator))
    }

    override fun refreshData() {
        _dataFetchObservable.refresh()
    }
}