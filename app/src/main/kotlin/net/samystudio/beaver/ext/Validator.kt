package net.samystudio.beaver.ext

import android.util.Patterns

fun <T> T.validate(validator: IValidator<T>): Boolean = validator.validate(this)

interface IValidator<in T>
{
    fun validate(any: T?): Boolean
}

class EmailValidator : IValidator<CharSequence>
{
    override fun validate(any: CharSequence?): Boolean =
        !any.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(any).matches()
}

class PasswordValidator : IValidator<CharSequence>
{
    override fun validate(any: CharSequence?): Boolean = !any.isNullOrBlank() && any!!.length >= 8
}

