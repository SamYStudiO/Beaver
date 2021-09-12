package net.samystudio.beaver.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.UserManager
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    userManager: UserManager,
    googleApiAvailability: GoogleApiAvailability,
) : ViewModel() {
    private val _userConnectedStated = MutableStateFlow(true)
    val userConnectedState: StateFlow<Boolean> = _userConnectedStated
    private val _googleApiAvailabilityState = MutableStateFlow<AsyncState>(AsyncState.Idle)
    val googleApiAvailabilityState: StateFlow<AsyncState> = _googleApiAvailabilityState

    init {
        viewModelScope.launch {
            userManager.userConnectedFlow.collect {
                _userConnectedStated.emit(it)
            }

            _googleApiAvailabilityState.emit(AsyncState.Started)
            val status: Int = googleApiAvailability.isGooglePlayServicesAvailable(context)
            if (status == ConnectionResult.SUCCESS)
                _googleApiAvailabilityState.emit(AsyncState.Completed)
            else
                _googleApiAvailabilityState.emit(
                    AsyncState.Failed(
                        GoogleApiAvailabilityException(
                            status,
                            googleApiAvailability.isUserResolvableError(status),
                            googleApiAvailability
                        )
                    )
                )
            _googleApiAvailabilityState.emit(AsyncState.Idle)
        }
    }

    class GoogleApiAvailabilityException(
        val status: Int,
        val isResolvable: Boolean,
        val googleApiAvailability: GoogleApiAvailability
    ) : RuntimeException("Google Api missing, can't go any further!")
}
