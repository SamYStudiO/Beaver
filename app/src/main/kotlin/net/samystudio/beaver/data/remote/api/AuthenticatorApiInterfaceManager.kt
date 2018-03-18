package net.samystudio.beaver.data.remote.api

import io.reactivex.Observable
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.remote.CompletableRequestState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorApiInterfaceManager
@Inject
constructor(
    private val userManager: UserManager,
    private val authenticatorApiInterface: AuthenticatorApiInterface)
{
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