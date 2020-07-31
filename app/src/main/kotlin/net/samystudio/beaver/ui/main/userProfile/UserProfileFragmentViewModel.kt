package net.samystudio.beaver.ui.main.userProfile

import androidx.hilt.lifecycle.ViewModelInject
import io.reactivex.rxjava3.core.Observable
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel

class UserProfileFragmentViewModel @ViewModelInject constructor(private val userManager: UserManager) :
    BaseFragmentViewModel() {
    fun <T : UserProfileUserFlow> addUserFlow(observable: Observable<T>) {
        disposables.add(observable.forEach { if (it is UserProfileUserFlow.Disconnect) userManager.disconnect() })
    }
}