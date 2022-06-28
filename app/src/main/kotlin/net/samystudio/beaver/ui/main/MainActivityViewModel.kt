package net.samystudio.beaver.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.repository.TokenException
import net.samystudio.beaver.data.repository.TokenRepository
import net.samystudio.beaver.data.repository.UserRepository
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.util.TriggerAsyncStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val googleApiAvailability: GoogleApiAvailability,
    tokenRepository: TokenRepository,
    userRepository: UserRepository,
    @ApplicationContext
    context: Context
) : ViewModel() {
    private val triggerInitializationFlow = TriggerAsyncStateFlow(true) { _initializationFlow }
    private val _initializationFlow: Flow<AsyncState> = flow {
        val status: Int = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (status != ConnectionResult.SUCCESS)
            throw  GoogleApiAvailabilityException(
                googleApiAvailability.isUserResolvableError(status),
                googleApiAvailability
            )

        try {
            tokenRepository.refresh()
        } catch (e: Exception) {
            // If an authentication error occurred we can stop here
            // otherwise swallow error.
            if (e is TokenException)
                throw e
            else
                e.printStackTrace()
        }

        try {
            userRepository.refresh()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        emit(Unit)
    }.toAsyncState().shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000))
    val initializationFlow: Flow<AsyncState> = triggerInitializationFlow

    val userConnectedState: Flow<Boolean> = userRepository.optionalDataFlow.map { it.isPresent }

    fun initialize() {
        triggerInitializationFlow.trigger()
    }

    fun logScreen(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }
}

class GoogleApiAvailabilityException(
    val isResolvable: Boolean,
    val googleApiAvailability: GoogleApiAvailability
) : RuntimeException("Google Api missing, can't go any further!")
