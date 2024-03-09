package com.android.teachnooktodo.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUser(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}

