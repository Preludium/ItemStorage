package pl.mik.itemstorage.database.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable
import java.lang.reflect.Constructor

@Entity(tableName = "items",
    indices = [Index(value = ["name"], unique = true)],
    foreignKeys = [ForeignKey(entity = Box::class, parentColumns = ["id"], childColumns = ["box_id"], onDelete = CASCADE)])
data class Item (
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "category")
    var category: String?,
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "box_id")
    var boxId: Int = 0,
    @ColumnInfo(name = "EAN_UPC_code")
    var ean_upc_code: String?
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
