package pl.mik.itemstorage.database.entities

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "localizations",
    indices = [Index(value = ["name"], unique = true)],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
data class Localization (
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "user_id")
    var userId: Int
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}