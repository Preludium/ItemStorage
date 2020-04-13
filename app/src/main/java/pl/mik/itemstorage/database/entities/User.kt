package pl.mik.itemstorage.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users",
    indices = [Index(value = ["name"], unique = true)])
data class User (
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "password")
    var password: String
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
