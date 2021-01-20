package net.samystudio.beaver.ui.main.userProfile

import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseDisposablesViewModel() {
    val userLiveData =
        userManager.getUser()
            .toResultAsyncState()
            .toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()

    fun disconnect() {
        userManager.disconnect()
    }
}
