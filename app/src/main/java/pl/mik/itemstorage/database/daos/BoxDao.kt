package pl.mik.itemstorage.database.daos

import androidx.room.*
import pl.mik.itemstorage.database.entities.Box

@Dao
interface BoxDao {
    @Query("SELECT * FROM boxes WHERE id = :id")
    fun getBoxById(id: Int?): Box

    @Query("SELECt * FROM boxes WHERE user_id = :user_id")
    fun getAllByUserId(user_id: Int): List<Box>

    @Query("SELECT * FROM boxes WHERE name = :name")
    fun getBoxByName(name: String): Box

    @Query("SELECT * FROM boxes WHERE localization_id = :localizationId")
    fun getAllBoxesByLocalizationId(localizationId: Int): List<Box>

    @Query("SELECT qr_code FROM boxes")
    fun getAllQRCodes(): List<String>

    @Query("SELECT name FROM boxes WHERE name NOT LIKE :name")
    fun getTheRestOfNames(name: String): List<String>

    @Query("SELECT qr_code FROM boxes WHERE qr_code NOT LIKE :qr_code")
    fun getTheRestOfQRCodes(qr_code: String): List<String>

    @Insert
    fun insert(box: Box)

    @Update
    fun update(box: Box)

    @Delete
    fun delete(box: Box)
}