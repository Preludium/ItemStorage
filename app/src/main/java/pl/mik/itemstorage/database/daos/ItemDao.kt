package pl.mik.itemstorage.database.daos

import androidx.room.*
import pl.mik.itemstorage.database.entities.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: Int): Item

    @Query("SELECT * FROM items WHERE name = :name")
    fun getItemByName(name: String): Item

    @Query("SELECT * FROM items WHERE box_id = :box_id")
    fun getAllItemsByBoxId(box_id: Int): List<Item>

    @Query("SELECT name FROM items WHERE name NOT LIKE :name")
    fun getTheRestOfNames(name: String): List<String>

    @Query("SELECT EAN_UPC_code FROM items")
    fun getAllEanUpcCodes(): String

    @Query("SELECT EAN_UPC_code FROM items WHERE EAN_UPC_code NOT LIKE :EAN_UPC_code")
    fun getTheRestOfEanUpcCodes(EAN_UPC_code: String): List<String>

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)
}