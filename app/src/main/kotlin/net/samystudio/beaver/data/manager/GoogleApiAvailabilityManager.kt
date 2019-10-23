package net.samystudio.beaver.data.manager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import net.samystudio.beaver.di.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class GoogleApiAvailabilityManager @Inject constructor(
    private val activity: AppCompatActivity,
    private val googleApiAvailability: GoogleApiAvailability
) {
    private var emitter: CompletableEmitter? = null

    /**
     * Async request to make sure everything is fine with Google apis otherwise we should notify
     * user app is incompatible
     */
    fun isAvailable(): Completable {
        return Completable.create { emitter ->
            val status: Int = googleApiAvailability.isGooglePlayServicesAvailable(activity)
            if (status == ConnectionResult.SUCCESS) emitter.onComplete()
            else {
                if (googleApiAvailability.isUserResolvableError(status)) {
                    googleApiAvailability.getErrorDialog(activity, status, REQUEST_CODE) {
                        this.emitter = emitter
                        emitter.onError(ERROR)
                    }.show()
                } else emitter.onError(ERROR)
            }
        }
    }

    /**
     * Called by Activity when [REQUEST_CODE] result is send to it.
     */
    fun onDialogResult(requestCode: Int, resultCode: Int) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                emitter?.onComplete()
            else
                emitter?.onError(ERROR)
        }
    }

    companion object {
        private const val REQUEST_CODE = 654
        private val ERROR = Throwable("Google Api missing, can't go any further!")
    }
}