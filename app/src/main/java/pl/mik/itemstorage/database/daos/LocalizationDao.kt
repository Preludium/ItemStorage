package pl.mik.itemstorage.database.daos

import androidx.room.*
import pl.mik.itemstorage.database.entities.Localization

@Dao
interface LocalizationDao {
    @Query("SELECT * FROM localizations WHERE id = :id")
    fun getLocalizationById(id: Int): Localization

    @Query("SELECT * FROM localizations WHERE name = :name")
    fun getLocalizationByName(name: String): Localization

    @Query("SELECT * FROM localizations WHERE user_id = :user_id")
    fun getAllByUserId(user_id: Int): List<Localization>

    @Query("SELECT name FROM localizations WHERE name NOT LIKE :name")
    fun getTheRestOfNames(name: String): List<String>

    @Insert
    fun insert(image: Localization)

    @Update
    fun update(image: Localization)

    @Delete
    fun delete(image: Localization)
}