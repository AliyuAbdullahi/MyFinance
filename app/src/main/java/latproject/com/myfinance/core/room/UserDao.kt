package latproject.com.myfinance.core.room

import android.arch.persistence.room.*

@Dao
interface UserDao {

    @Query("select * from User LIMIT 1")
    fun getUser(): User

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Insert
    fun addUser(user: User)
}