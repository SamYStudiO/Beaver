package net.samystudio.beaver.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.toLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.repository.TokenException
import net.samystudio.beaver.data.repository.TokenRepository
import net.samystudio.beaver.data.repository.UserRepository
import net.samystudio.beaver.data.toAsyncState
import net.samystudio.beaver.ui.base.viewmodel.BaseDisposablesViewModel
import net.samystudio.beaver.ui.common.viewmodel.toSingleLiveEvent
import net.samystudio.beaver.ui.common.viewmodel.toTriggerLiveData
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val googleApiAvailability: GoogleApiAvailability,
    tokenRepository: TokenRepository,
    userRepository: UserRepository,
    @ApplicationContext
    context: Context
) : BaseDisposablesViewModel() {
    /**
     * Async observable to make sure everything is fine with Google apis otherwise we should notify
     * user app is incompatible.
     */
    private val availabilityObservable: Single<Boolean> = Single.fromCallable {
        val status: Int = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (status == ConnectionResult.SUCCESS)
            true
        else
            throw GoogleApiAvailabilityException(
                googleApiAvailability.isUserResolvableError(status),
                googleApiAvailability
            )
    }
    private val _appDataLiveData =
        // Check google api -> required
        availabilityObservable.ignoreElement()
            // If connected refresh token and user data
            .andThen(
                // First check token, if no error do user related stuffs.
                tokenRepository.refresh().ignoreElement()
                    .onErrorResumeNext {
                        // If an authentication error occurred we can stop here
                        // otherwise swallow error.
                        if (it is TokenException)
                            Completable.error(it)
                        else
                            Completable.complete()
                    }
                    // Get user info -> required
                    .andThen(userRepository.refresh().ignoreElement())
            )
            .toAsyncState()
            .toTriggerLiveData()
            .apply { addTo(disposables) }

    /**
     * A [LiveData] to listen when all app data are refreshed, some data may be required to run app
     * properly and others optional. In case required app data are missing a [AsyncState.Failed]
     * value will be set and app should not be start and user informed we can't run app for now.
     */
    val appDataLiveData: LiveData<AsyncState> =
        _appDataLiveData.toSingleLiveEvent()

    /**
     * User status [LiveData] containing a [Boolean] indicating if user is connected.
     */
    val userStatusLiveData =
        userRepository.optionalDataObservable.toFlowable(BackpressureStrategy.LATEST)
            .map { it.isPresent }
            .onErrorComplete()
            .toLiveData()
            .distinctUntilChanged()
    val userIsConnected =
        userStatusLiveData.value ?: false

    fun refreshData() {
        _appDataLiveData.trigger()
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
