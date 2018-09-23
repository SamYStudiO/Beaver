package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.remote.api.AuthenticatorApiInterface
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @see [net.samystudio.beaver.di.module.NetworkModule.provideAuthenticatorApiInterface]
 */
@Singleton
class AuthenticatorRepositoryManager
@Inject
constructor(
    private val userManager: UserManager,
    private val authenticatorApiInterface: AuthenticatorApiInterface
) {
    fun signIn(email: String, password: String): Observable<AsyncState> =
        authenticatorApiInterface
            .signIn(email, password)
            .toObservable()
            .onErrorReturnItem("token") // TODO remove this line, for debug only
            .map {
                userManager.connect(email, password, it)
                AsyncState.Completed
            }
            .cast(AsyncState::class.java)
            .onErrorReturn { AsyncState.Error(it) }
            .startWith(AsyncState.Started)

    fun signUp(email: String, password: String): Observable<AsyncState> =
        authenticatorApiInterface
            .signUp(email, password)
            .toObservable()
            .onErrorReturnItem("token") // TODO remove this line, for debug only
            .map {
                userManager.createAccount(email, password)
                AsyncState.Completed
            }
            .cast(AsyncState::class.java)
            .onErrorReturn { AsyncState.Error(it) }
            .startWith(AsyncState.Started)
}