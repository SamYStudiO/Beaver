package net.samystudio.beaver.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey val id: Int,
    val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)
