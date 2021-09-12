package net.samystudio.beaver.data.repository

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import net.samystudio.beaver.data.local.UserDao
import net.samystudio.beaver.data.manager.PreferencesManager
import net.samystudio.beaver.data.model.User
import net.samystudio.beaver.data.remote.UserApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val userDao: UserDao,
    private val userApiInterface: UserApiInterface
) : BaseRepository() {
    /**
     * Get a [Flow] to observe user changes.
     */
    @DelicateCoroutinesApi
    @ExperimentalCoroutinesApi
    val userFlow =
        preferencesManager.accountName()
            .filter { it.isNotBlank() }
            .flatMapLatest { userDao.getUserFlowByEmail(it) }
            .shareIn(
                GlobalScope,
                SharingStarted.WhileSubscribed(),
                1
            )

    /**
     * Get user locally or remotely if there is no local user or user is invalidated locally.
     */
    suspend fun getUser() =
        if (isLocalInvalidate) getUserRemote()
        else preferencesManager.accountName().lastOrNull()?.let {
            if (it.isBlank()) null else userDao.getUserByEmail(it)
        } ?: getUserRemote()

    /**
     * Get user remotely.
     */
    suspend fun getUserRemote() =
        userApiInterface.getUser().apply {
            updatePreferences(this)
            userDao.insertUser(this)
            isLocalInvalidate = false
        }

    suspend fun updateUser(user: User) {
        userApiInterface.patchUser(user)
        updatePreferences(user)
        userDao.insertUser(user)
        isLocalInvalidate = false
    }

    private suspend fun updatePreferences(user: User) {
        preferencesManager.updateAccountName(user.email)
    }
}
