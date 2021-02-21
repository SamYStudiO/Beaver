@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.data.manager

import android.content.ComponentCallbacks2
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import net.samystudio.beaver.data.TrimMemory
import net.samystudio.beaver.data.local.SharedPreferencesHelper
import net.samystudio.beaver.data.local.UserDao
import net.samystudio.beaver.data.model.Token
import net.samystudio.beaver.data.model.User
import net.samystudio.beaver.data.remote.AuthenticatorApiInterfaceImpl
import net.samystudio.beaver.data.remote.UserApiInterfaceImpl
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val authenticationApiInterfaceImpl: AuthenticatorApiInterfaceImpl,
    private val userApiInterfaceImpl: UserApiInterfaceImpl,
    private val userDao: UserDao,
) : TrimMemory {
    private val _statusObservable: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(isConnected)
    val statusObservable: Observable<Boolean> = _statusObservable.distinctUntilChanged()
    private val _userObservable = BehaviorSubject.create<User>()
    val userObservable: Observable<User> = _userObservable.distinctUntilChanged()
    val token
        get() = sharedPreferencesHelper.accountToken
    val isConnected
        get() = token != null
    private var userCache: User? = null

    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN,
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL,
            -> clearCache()
            else -> {
            }
        }
    }

    fun signIn(email: String, password: String): Completable =
        authenticationApiInterfaceImpl.signIn(email, password).doOnSuccess { writeToken(it) }
            .ignoreElement()

    fun signUp(email: String, password: String): Completable =
        authenticationApiInterfaceImpl.signUp(email, password).doOnSuccess { writeToken(it) }
            .ignoreElement()

    fun refreshToken(): Completable =
        (
            token?.let { token ->
                authenticationApiInterfaceImpl.refreshToken(token.refreshToken)
                    .doOnSuccess { writeToken(it) }
                    .ignoreElement()
            } ?: Completable.error(TokenException())
            )
            .doOnError {
                Timber.w(it, "An error occurred refreshing token")
                disconnect()
            }

    fun getUserRemote(): Single<User> =
        userApiInterfaceImpl.getUser().doOnSuccess { writeUser(it) }

    fun getUser(): Single<User> =
        userCache?.let { user -> Single.just(user) }
            ?: userDao.getUserByEmail(sharedPreferencesHelper.accountName.get())
                .subscribeOn(Schedulers.io()).flatMap { users ->
                    if (users.isNotEmpty()) {
                        users.first().let {
                            writeUser(it)
                            Single.just(it)
                        }
                    } else getUserRemote()
                }

    fun disconnect() {
        sharedPreferencesHelper.accountToken = null
        _statusObservable.onNext(false)
    }

    fun clearCache() {
        userCache = null
    }

    private fun writeUser(user: User) {
        userDao.insertUser(user)
        sharedPreferencesHelper.accountName.set(user.email)
        userCache = user
        _userObservable.onNext(user)
    }

    private fun writeToken(token: Token) {
        sharedPreferencesHelper.accountToken = token
    }

    class TokenException : RuntimeException("No valid token available")
}
