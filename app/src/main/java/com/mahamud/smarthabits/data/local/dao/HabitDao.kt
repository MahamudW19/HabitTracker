package com.mahamud.smarthabits.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mahamud.smarthabits.data.local.entity.DailyLogEntity
import com.mahamud.smarthabits.data.local.entity.HabitEntity
import com.mahamud.smarthabits.data.local.entity.HabitWithLogs
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    // Home screen uses this Flow so the list updates automatically after any change.
    @Transaction
    @Query("SELECT * FROM habits ORDER BY id DESC")
    fun getAllHabits(): Flow<List<HabitWithLogs>>

    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: Long): Flow<HabitWithLogs?>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitEntityById(habitId: Long): HabitEntity?

    @Insert
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    // We only need one log per habit per day, so the repository checks today's date.
    @Query("SELECT * FROM daily_logs WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getDailyLogForDate(habitId: Long, date: String): DailyLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyLog(log: DailyLogEntity)

    @Query("DELETE FROM daily_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteDailyLogForDate(habitId: Long, date: String)
}
