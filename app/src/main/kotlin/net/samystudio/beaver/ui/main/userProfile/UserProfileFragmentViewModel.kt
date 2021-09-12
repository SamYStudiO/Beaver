package net.samystudio.beaver.ui.main.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.common.viewmodel.TriggerOutStateFlow
import net.samystudio.beaver.ui.common.viewmodel.TriggerStateFlow
import javax.inject.Inject

@HiltViewModel
class UserProfileFragmentViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel() {
    val disconnectState = TriggerStateFlow {
        userManager.disconnect()
    }
    val userState = TriggerOutStateFlow(true) {
        userManager.getUser()
    }

    fun disconnect() {
        viewModelScope.launch {
            disconnectState.trigger()
        }
    }
}
