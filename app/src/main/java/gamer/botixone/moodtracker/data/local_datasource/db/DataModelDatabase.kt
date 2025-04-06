package gamer.botixone.moodtracker.data.local_datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gamer.botixone.moodtracker.data.local_datasource.dao.MoodDao
import gamer.botixone.moodtracker.data.local_datasource.dao.QuestionDao
import gamer.botixone.moodtracker.data.local_datasource.model.MoodEntity
import gamer.botixone.moodtracker.data.local_datasource.model.QuestionEntity

@Database(entities =
    [
        MoodEntity::class,
        QuestionEntity::class
    ],
    version = 1, exportSchema = false
)

abstract  class DataModelDatabase: RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile private var instance: DataModelDatabase? = null
        private const val DB_NAME = "moodTracker.db"

        fun getDatabase(context: Context): DataModelDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, DataModelDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}