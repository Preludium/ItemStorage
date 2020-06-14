package pl.mik.itemstorage.database

import android.content.Context
import android.os.Environment
import androidx.room.*
import pl.mik.itemstorage.database.daos.*
import pl.mik.itemstorage.database.entities.*
import java.io.File
import java.io.FileOutputStream

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
        private const val DATABASE_NAME = "database.db"

        fun getAppDataBase(context: Context): AppDatabase? {
            if (instance == null){
                synchronized(AppDatabase ::class) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build()
                }
            }
            return instance
        }

        private fun copyDataFromOneToAnother(fromPath: String, toPath: String) {
            val inStream = File(fromPath).inputStream()
            val outStream = FileOutputStream(toPath)

            inStream.use { input ->
                outStream.use { output ->
                    input.copyTo(output)
                }
            }
        }

        fun exportDatabaseFile(context: Context) {
            try {
                copyDataFromOneToAnother(context.getDatabasePath(DATABASE_NAME).path,
                    Environment.getExternalStorageDirectory().path + "/Download/" + DATABASE_NAME)
                copyDataFromOneToAnother(context.getDatabasePath("$DATABASE_NAME-shm").path,
                    Environment.getExternalStorageDirectory().path + "/Download/" + DATABASE_NAME + "-shm")
                copyDataFromOneToAnother(context.getDatabasePath("$DATABASE_NAME-wal").path,
                    Environment.getExternalStorageDirectory().path + "/Download/" + DATABASE_NAME + "-wal")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun importDatabaseFile(context: Context) {
            try {
                copyDataFromOneToAnother(Environment.getExternalStorageDirectory()
                    .path + "/Download/" + DATABASE_NAME, context.getDatabasePath(DATABASE_NAME).path)

                copyDataFromOneToAnother(Environment.getExternalStorageDirectory()
                    .path + "/Download/" + DATABASE_NAME + "-shm", context.getDatabasePath("$DATABASE_NAME-shm").path)

                copyDataFromOneToAnother(Environment.getExternalStorageDirectory()
                    .path + "/Download/" + DATABASE_NAME + "-wal", context.getDatabasePath("$DATABASE_NAME-wal").path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}