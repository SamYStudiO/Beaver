package net.samystudio.beaver.ui.main.authenticator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.PreferencesManager
import net.samystudio.beaver.data.repository.TokenRepository
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.util.TriggerInAsyncStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthenticatorFragmentViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    preferencesManager: PreferencesManager,
) : ViewModel() {
    private val _loginState = TriggerInAsyncStateFlow<Pair<String, String>> {
        flow {
            emit(tokenRepository.login(it.first, it.second))
        }.toAsyncState()
    }
    val loginState: Flow<AsyncState> = _loginState
    private val _registerState = TriggerInAsyncStateFlow<Pair<String, String>> {
        flow {
            emit(tokenRepository.register(it.first, it.second))
        }.toAsyncState()
    }
    val registerState: Flow<AsyncState> = _registerState
    val defaultAccountName: Flow<String> = preferencesManager.server.map {
        it.defaultEmail
    }
    val defaultAccountPassword: Flow<String> = preferencesManager.server.map {
        it.defaultPassword
    }
    val accountName: Flow<String> = preferencesManager.accountId

    fun register(email: String, password: String) {
        _loginState.trigger(email to password)
    }

    fun login(email: String, password: String) {
        _registerState.trigger(email to password)
    }
}
