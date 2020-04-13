package pl.mik.itemstorage.database.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable
import java.util.*

@Entity(tableName = "images_box",
    indices = [Index(value = ["box_id"])],
    foreignKeys = [ForeignKey(entity = Box::class, parentColumns = ["id"], childColumns = ["box_id"], onDelete = CASCADE)])
data class ImageBox (
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray,
    @ColumnInfo(name = "box_id")
    var boxId: Int = 0,
    @TypeConverters(TypeConverter::class)
    @ColumnInfo(name = "created_at")
    var createdAt: Date
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}