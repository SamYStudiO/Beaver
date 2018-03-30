package net.samystudio.beaver.ui.main.authenticator

sealed class AuthenticatorUserFlow {
    data class SignIn(val email: String, val password: String) : AuthenticatorUserFlow()
    data class SignUp(val email: String, val password: String) : AuthenticatorUserFlow()
}