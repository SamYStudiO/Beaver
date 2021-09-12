package net.samystudio.beaver.ui.main.authenticator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.samystudio.beaver.data.manager.PreferencesManager
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.common.viewmodel.TriggerInStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthenticatorFragmentViewModel @Inject constructor(
    private val userManager: UserManager,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    val signInState = TriggerInStateFlow<Pair<String, String>> {
        userManager.signIn(it.first, it.second)
    }
    val signUpState = TriggerInStateFlow<Pair<String, String>> {
        userManager.signUp(it.first, it.second)
    }
    val accountName = preferencesManager.accountName(true)

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInState.trigger(email to password)
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            signUpState.trigger(email to password)
        }
    }
}
