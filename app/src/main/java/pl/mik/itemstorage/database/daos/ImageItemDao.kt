package pl.mik.itemstorage.database.daos

import androidx.room.*
import pl.mik.itemstorage.database.entities.ImageItem

@Dao
interface ImageItemDao {
    @Query("SELECT * FROM images_item WHERE id = :id")
    fun getImageById(id: Int): ImageItem

    @Query("SELECT * FROM images_item WHERE item_id = :item_id")
    fun getImagesByItemId(item_id: Int): List<ImageItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(images: ArrayList<ImageItem>)

    @Insert
    fun insert(image: ImageItem)

    @Update
    fun update(image: ImageItem)

    @Delete
    fun delete(image: ImageItem)
}