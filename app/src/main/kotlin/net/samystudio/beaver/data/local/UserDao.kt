package net.samystudio.beaver.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUser(id: Long): Single<List<User>>

    @Query("SELECT * FROM User WHERE email = :email")
    fun getUserByEmail(email: String): Single<List<User>>
}
