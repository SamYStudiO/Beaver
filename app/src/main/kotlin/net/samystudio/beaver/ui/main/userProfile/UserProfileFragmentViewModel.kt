package net.samystudio.beaver.ui.main.userProfile

import androidx.lifecycle.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseDisposablesViewModel() {
    val userLiveData =
        userManager.getUser()
            .toResultAsyncState()
            .toLiveData()

    fun disconnect() {
        userManager.disconnect()
    }
}
