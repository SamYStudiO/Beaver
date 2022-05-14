package net.samystudio.beaver.ui.main.userProfile

import dagger.hilt.android.lifecycle.HiltViewModel
import net.samystudio.beaver.data.repository.UserRepository
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveData
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseDisposablesViewModel() {
    val userLiveData =
        userRepository.dataObservable
            .toResultAsyncState()
            .toTriggerLiveData()

    fun disconnect() {
        userRepository.logout()
    }
}
