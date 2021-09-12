package net.samystudio.beaver.ui.main.userProfile

import dagger.hilt.android.lifecycle.HiltViewModel
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveData
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseDisposablesViewModel() {
    val userLiveData =
        userManager.getUser()
            .toResultAsyncState()
            .toTriggerLiveData()

    fun disconnect() {
        userManager.disconnect()
    }
}
