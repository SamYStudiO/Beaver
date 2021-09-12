package net.samystudio.beaver.data.model

val TOKEN_DEBUG = Token.newBuilder()
    .setTokenType("Bearer")
    .setAccessToken("accessToken")
    .setExpireIn(123456789)
    .setRefreshToken("refreshToken")
    .build()
