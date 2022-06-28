package net.samystudio.beaver.data.repository

import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import net.samystudio.beaver.data.local.UserDao
import net.samystudio.beaver.data.manager.PreferencesManager
import net.samystudio.beaver.data.model.User
import net.samystudio.beaver.data.remote.UserApiInterface
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours

@Singleton
class UserRepository @Inject constructor(
    private val tokenRepository: Lazy<TokenRepository>,
    private val userDao: UserDao,
    private val preferencesManager: PreferencesManager,
    private val userApiInterface: UserApiInterface,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
    sharedPreferences: SharedPreferences,
) : DataRepository<User>(
    1.hours,
    SharedPreferencesDataRepositoryIntegrityHolder(sharedPreferences),
    SharedPreferencesDataRepositoryIntegrityHolder(sharedPreferences),
) {

    override suspend fun getRemoteData(): User =
        userApiInterface.getUser()

    override suspend fun getLocalData(): User? =
        userDao.getUserByEmail(preferencesManager.accountId.first())

    override suspend fun setLocalData(data: User?) = withContext(Dispatchers.IO) {
        when {
            // Connected
            data != null -> {
                preferencesManager.updateAccountId(data.email)
                userDao.insertUser(data)
            }
            // Disconnected
            else -> {
                preferencesManager.clearAccountId()
                tokenRepository.get().clear()
            }
        }
    }

    override fun refreshed() {
        val id = data?.id?.toString()
        firebaseAnalytics.setUserId(id)
        crashlytics.setUserId(id ?: "")
    }

    /**
     * Alias for [clear], this is the right place to call any remote server logout api if required
     * before calling [clear].
     */
    suspend fun logout() = clear()
}
