package net.samystudio.beaver.data.remote

import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorApiManager
@Inject constructor(private val authenticatorApiInterface: AuthenticatorApiInterface)
{
    fun signIn(email: String, password: String): Single<String> =
        authenticatorApiInterface.signIn(email, password).onErrorReturnItem("toctoctoken")

    fun signUp(email: String,
               password: String) =
        authenticatorApiInterface.signUp(email, password)
}