package net.samystudio.beaver.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Long,
    val email: String,
    val firstname: String,
    val lastname: String
)
