package net.samystudio.beaver.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserCompletable(user: User): Completable

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserByIdSingle(id: Long): Single<User>
}
