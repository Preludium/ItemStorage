package pl.mik.itemstorage.database.daos

import androidx.room.*
import pl.mik.itemstorage.database.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): User

    @Query("SELECT * FROM users WHERE name = :name")
    fun getUserByName(name: String): User

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}