package net.samystudio.beaver.data.remote

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.data.toAsyncState
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface AuthenticatorApiInterface {
    @POST("signIn")
    @FormUrlEncoded
    fun signIn(@Field("email") email: String, @Field("password") password: String): Single<String>

    @POST("signUp")
    @FormUrlEncoded
    fun signUp(@Field("email") email: String, @Field("password") password: String): Single<String>
}

@Singleton
class AuthenticatorApiInterfaceImpl
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