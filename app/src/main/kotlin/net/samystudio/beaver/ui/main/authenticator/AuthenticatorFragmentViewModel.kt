package net.samystudio.beaver.ui.main.authenticator

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.model.Server
import net.samystudio.beaver.data.repository.TokenRepository
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.TriggerDataLiveData
import net.samystudio.beaver.ui.common.viewmodel.toSingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class AuthenticatorFragmentViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) : BaseDisposablesViewModel() {
    private val _loginLiveData = TriggerDataLiveData<Pair<String, String>, AsyncState> {
        tokenRepository.login(it.first, it.second).toAsyncState()
    }
    val loginLiveData: LiveData<AsyncState> = _loginLiveData.toSingleLiveEvent()
    private val _registerLiveData = TriggerDataLiveData<Pair<String, String>, AsyncState> {
        tokenRepository.register(it.first, it.second).toAsyncState()
    }
    val registerLiveData: LiveData<AsyncState> = _registerLiveData.toSingleLiveEvent()
    val server: Server
        get() = sharedPreferencesHelper.server

    fun login(email: String, password: String) {
        _loginLiveData.trigger(email to password)
    }

    fun register(email: String, password: String) {
        _registerLiveData.trigger(email to password)
    }
}
