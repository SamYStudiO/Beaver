package net.samystudio.beaver.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import net.samystudio.beaver.data.model.User

@Database(entities = [User::class], version = 1)
abstract class BeaverDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}