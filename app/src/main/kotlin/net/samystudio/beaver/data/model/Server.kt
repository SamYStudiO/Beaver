package net.samystudio.beaver.data.model

data class Server(
    val name: String,
    val url: String,
    val defaultForBuildType: String? = null,
    val testingFeatures: Boolean = false,
    val defaultEmail: String? = null,
    val defaultPassword: String? = null,
)
