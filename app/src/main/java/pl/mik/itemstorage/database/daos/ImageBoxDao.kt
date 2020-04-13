package pl.mik.itemstorage.database.daos

import androidx.room.*
import pl.mik.itemstorage.database.entities.ImageBox

@Dao
interface ImageBoxDao {
    @Query("SELECT * FROM images_box WHERE id = :id")
    fun getImageById(id: Int): ImageBox

    @Query("SELECT * FROM images_box WHERE box_id = :box_id")
    fun getImagesByBoxId(box_id: Int): List<ImageBox>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(images: ArrayList<ImageBox>)

    @Insert
    fun insert(image: ImageBox)

    @Update
    fun update(image: ImageBox)

    @Delete
    fun delete(image: ImageBox)
}