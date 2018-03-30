package net.samystudio.beaver.data.manager

import io.reactivex.Observable
import net.samystudio.beaver.data.remote.CompletableRequestState
import net.samystudio.beaver.data.remote.api.AuthenticatorApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorRepositoryManager
@Inject
constructor(
    private val userManager: UserManager,
    private val authenticatorApiInterface: AuthenticatorApiInterface
) {
    fun signIn(email: String, password: String): Observable<CompletableRequestState> =
        authenticatorApiInterface
            .signIn(email, password)
            .toObservable()
            .map {
                userManager.onNewToken(email, password, it)
                CompletableRequestState.Complete()
            }
            .cast(CompletableRequestState::class.java)
            .onErrorReturn { CompletableRequestState.Error(it) }
            .startWith(CompletableRequestState.Start())

    fun signUp(email: String, password: String): Observable<CompletableRequestState> =
        authenticatorApiInterface
            .signUp(email, password)
            .toObservable()
            .map {
                userManager.onNewToken(email, password, it)
                CompletableRequestState.Complete()
            }
            .cast(CompletableRequestState::class.java)
            .onErrorReturn { CompletableRequestState.Error(it) }
            .startWith(CompletableRequestState.Start())
}