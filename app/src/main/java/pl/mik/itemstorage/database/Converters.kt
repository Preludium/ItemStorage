package pl.mik.itemstorage.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toBitmap(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    @TypeConverter
    fun toByteArray(image: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}