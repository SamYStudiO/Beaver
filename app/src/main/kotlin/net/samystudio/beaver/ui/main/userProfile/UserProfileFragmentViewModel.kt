package net.samystudio.beaver.ui.main.userProfile

import io.reactivex.Observable
import net.samystudio.beaver.R
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import javax.inject.Inject

@FragmentScope
class UserProfileFragmentViewModel @Inject constructor() : BaseFragmentViewModel() {

    fun <T : UserProfileUserFlow> addUserFlow(observable: Observable<T>) {
        disposables.add(observable.map {
            if (it is UserProfileUserFlow.Disconnect) userManager.disconnect()
        }.subscribe())
    }

    override fun handleUserDisconnected() {
        super.handleUserDisconnected()
        navigate(NavigationRequest.Push(R.id.action_global_authenticator))
    }
}