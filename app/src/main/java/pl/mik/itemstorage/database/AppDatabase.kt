package pl.mik.itemstorage.database

import android.content.Context
import androidx.room.*
import pl.mik.itemstorage.database.daos.*
import pl.mik.itemstorage.database.entities.*

@Database(entities = [Box::class, Item::class, User::class, Localization::class, ImageItem::class, ImageBox::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun boxes(): BoxDao
    abstract fun items(): ItemDao
    abstract fun users(): UserDao
    abstract fun localizations(): LocalizationDao
    abstract fun imagesItem(): ImageItemDao
    abstract fun imagesBox(): ImageBoxDao

    companion object {
        private var instance: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (instance == null){
                synchronized(AppDatabase ::class) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase ::class.java, "database.db").allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}