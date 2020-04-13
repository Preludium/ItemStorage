package pl.mik.itemstorage.database.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable

@Entity(tableName = "boxes",
    indices = [Index(value = ["name"], unique = true)],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = CASCADE),
        ForeignKey(entity = Localization::class, parentColumns = ["id"], childColumns = ["localization_id"], onDelete = CASCADE)])
data class Box (
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "qr_code")
    var qrCode: String?,
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "user_id")
    var userId: Int,
    @ColumnInfo(name = "localization_id")
    var localizationId: Int
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}