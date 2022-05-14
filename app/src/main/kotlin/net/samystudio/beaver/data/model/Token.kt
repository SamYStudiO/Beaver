package net.samystudio.beaver.data.model

data class Token(
    val tokenType: String,
    val expireIn: Long,
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        val DEBUG = Token("Bearer", 123456789, "accessToken", "refreshToken")
    }
}

val Token?.isValid: Boolean
    get() = this != null && accessToken.isNotBlank() && refreshToken.isNotBlank()
