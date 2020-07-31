package net.samystudio.beaver.data.manager

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.toAsyncState
import javax.inject.Inject

@ActivityScoped
class GoogleApiAvailabilityManager @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val googleApiAvailability: GoogleApiAvailability
) {
    /**
     * Async observable to make sure everything is fine with Google apis otherwise we should notify
     * user app is incompatible.
     */
    val availabilityObservable: Observable<AsyncState> = Single.create<Boolean> { emitter ->
            val status: Int =
                googleApiAvailability.isGooglePlayServicesAvailable(context)
            if (status == ConnectionResult.SUCCESS)
                emitter.onSuccess(true)
            else
                emitter.onError(
                    GoogleApiAvailabilityException(
                        status,
                        googleApiAvailability.isUserResolvableError(status),
                        googleApiAvailability
                    )
                )

        }.toAsyncState()
        .replay()
        .refCount()

    class GoogleApiAvailabilityException(
        val status: Int,
        val isResolvable: Boolean,
        val googleApiAvailability: GoogleApiAvailability
    ) : RuntimeException("Google Api missing, can't go any further!")
}