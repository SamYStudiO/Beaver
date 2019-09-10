package net.samystudio.beaver.ext

import android.util.Patterns

fun <T> T.validate(validator: Validator<T>): Boolean = validator.validate(this)

interface Validator<in T> {
    fun validate(any: T?): Boolean
}

private class EmailValidator : Validator<CharSequence> {
    override fun validate(any: CharSequence?): Boolean =
        !any.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(any).matches()
}

val EMAIL_VALIDATOR: Validator<CharSequence> = EmailValidator()

private class PasswordValidator : Validator<CharSequence> {
    override fun validate(any: CharSequence?): Boolean = !any.isNullOrBlank() && any.length >= 8
}

val PASSWORD_VALIDATOR: Validator<CharSequence> = PasswordValidator()

