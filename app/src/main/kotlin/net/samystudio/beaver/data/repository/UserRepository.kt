package net.samystudio.beaver.data.repository

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Lazy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.local.UserDao
import net.samystudio.beaver.data.model.User
import net.samystudio.beaver.data.remote.UserApiInterfaceImpl
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Singleton
class UserRepository @Inject constructor(
    private val tokenRepository: Lazy<TokenRepository>,
    private val userDao: UserDao,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val userApiInterfaceImpl: UserApiInterfaceImpl,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
) : DataRepository<User>(
    1.hours,
) {
    override fun getAge(): Duration =
        (System.currentTimeMillis() - sharedPreferencesHelper.userDate.get()).toDuration(
            DurationUnit.MILLISECONDS
        )

    override fun getRemoteDataSingle(): Single<User> =
        userApiInterfaceImpl.getUser()

    override fun getLocalDataSingle(): Single<User> =
        userDao.getUserByIdSingle(sharedPreferencesHelper.accountId.get())
            .subscribeOn(Schedulers.io())

    override fun setLocalDataSingle(data: User?): Completable? =
        when {
            // Connected
            data != null -> {
                sharedPreferencesHelper.accountId.set(data.id)
                sharedPreferencesHelper.userDate.set(System.currentTimeMillis())
                userDao.insertUserCompletable(data).subscribeOn(Schedulers.io())
            }
            // Disconnected
            else -> {
                sharedPreferencesHelper.accountId.delete()
                sharedPreferencesHelper.userDate.delete()
                tokenRepository.get().clear()
            }
        }

    override fun refreshed() {
        val id = data?.id?.toString()
        firebaseAnalytics.setUserId(id)
        crashlytics.setUserId(id ?: "")
    }

    /**
     * Alias for [clear].
     */
    fun logout(): Completable = clear()
}
