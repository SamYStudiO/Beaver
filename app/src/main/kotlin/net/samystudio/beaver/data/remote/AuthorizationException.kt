package net.samystudio.beaver.data.remote

import java.io.IOException

class AuthorizationException : IOException
{
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}