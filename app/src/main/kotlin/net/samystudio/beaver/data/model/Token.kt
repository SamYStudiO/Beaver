package net.samystudio.beaver.data.model

val TOKEN_DEBUG: Token = Token.newBuilder()
    .setTokenType("Bearer")
    .setAccessToken("accessToken")
    .setExpireIn(123456789)
    .setRefreshToken("refreshToken")
    .build()

val Token?.isValid: Boolean
    get() = this != null && accessToken.isNotBlank() && refreshToken.isNotBlank()
