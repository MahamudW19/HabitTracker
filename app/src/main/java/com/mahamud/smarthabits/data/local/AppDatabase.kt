package com.mahamud.smarthabits.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mahamud.smarthabits.data.local.dao.HabitDao
import com.mahamud.smarthabits.data.local.entity.DailyLogEntity
import com.mahamud.smarthabits.data.local.entity.HabitEntity

@Database(
    entities = [HabitEntity::class, DailyLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // A single database instance is enough for the whole app.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_habits.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
