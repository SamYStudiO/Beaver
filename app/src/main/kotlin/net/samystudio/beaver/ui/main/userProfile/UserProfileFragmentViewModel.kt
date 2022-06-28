package net.samystudio.beaver.ui.main.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import net.samystudio.beaver.data.repository.UserRepository
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.data.toResultAsyncState
import net.samystudio.beaver.util.TriggerAsyncStateFlow
import net.samystudio.beaver.util.TriggerResultAsyncStateFlow
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val logoutState = TriggerAsyncStateFlow{
        flow {
            emit(userRepository.logout())
        }.toAsyncState()
    }
    val userState = TriggerResultAsyncStateFlow(true) {
        flow {
            emit(userRepository.getAsyncData())
        }.toResultAsyncState()
    }

    fun disconnect() {
        logoutState.trigger()
    }
}
