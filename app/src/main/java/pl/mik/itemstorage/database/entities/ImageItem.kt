package pl.mik.itemstorage.database.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable
import java.util.*

@Entity(tableName = "images_item",
    indices = [Index(value = ["item_id"])],
    foreignKeys = [ForeignKey(entity = Item::class, parentColumns = ["id"], childColumns = ["item_id"], onDelete = CASCADE)])
data class ImageItem (
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray,
    @ColumnInfo(name = "item_id")
    var itemId: Int = 0,
    @TypeConverters(TypeConverter::class)
    @ColumnInfo(name = "created_at")
    var createdAt: Date
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}