package net.samystudio.beaver.ui.main.authenticator

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.common.viewmodel.TriggerDataLiveEvent

class AuthenticatorFragmentViewModel @ViewModelInject constructor(
    private val userManager: UserManager
) : ViewModel() {
    val signLiveData = TriggerDataLiveEvent<Pair<String, String>, AsyncState> {
        userManager.signIn(it.first, it.second).toFlowable()
    }

    override fun onCleared() {
        super.onCleared()
        signLiveData.dispose()
    }

    fun signIn(email: String, password: String) {
        signLiveData.trigger(email to password)
    }

    fun signUp(email: String, password: String) {
        signLiveData.trigger(email to password)
    }
}
