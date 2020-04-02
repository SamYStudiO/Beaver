package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.remote.AuthenticatorApiInterface
import net.samystudio.beaver.data.toAsyncState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @see net.samystudio.beaver.di.module.NetworkModule.provideAuthenticatorApiInterface
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
            .onErrorReturnItem("token") // TODO Remove this line, for debug only.
            .map { userManager.connect(email, password, it) }
            .toAsyncState()

    fun signUp(email: String, password: String): Observable<AsyncState> =
        authenticatorApiInterface
            .signUp(email, password)
            .onErrorReturnItem("token") // TODO Remove this line, for debug only.
            .map { userManager.createAccount(email, password) }
            .toAsyncState()
}