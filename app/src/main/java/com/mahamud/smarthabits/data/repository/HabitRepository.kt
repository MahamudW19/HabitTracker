package com.mahamud.smarthabits.data.repository

import com.mahamud.smarthabits.data.local.dao.HabitDao
import com.mahamud.smarthabits.data.local.entity.DailyLogEntity
import com.mahamud.smarthabits.data.local.entity.HabitEntity
import com.mahamud.smarthabits.data.local.entity.HabitWithLogs
import com.mahamud.smarthabits.util.calculateStreak
import com.mahamud.smarthabits.util.formatDisplayDate
import com.mahamud.smarthabits.util.isDoneToday
import com.mahamud.smarthabits.util.todayString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class HabitListItem(
    val id: Long,
    val name: String,
    val category: String,
    val doneToday: Boolean,
    val streak: Int
)

data class HabitDetailItem(
    val id: Long,
    val name: String,
    val category: String,
    val startDate: String,
    val doneToday: Boolean,
    val streak: Int
)

class HabitRepository(
    private val habitDao: HabitDao
) {
    // The repository maps Room data into simpler models for the Compose screens.
    fun getHabits(): Flow<List<HabitListItem>> {
        return habitDao.getAllHabits().map { habits ->
            habits.map { it.toListItem() }
        }
    }

    fun getHabitDetail(habitId: Long): Flow<HabitDetailItem?> {
        return habitDao.getHabitById(habitId).map { habit ->
            habit?.toDetailItem()
        }
    }

    fun getHabitForEdit(habitId: Long): Flow<HabitEntity?> {
        return habitDao.getHabitById(habitId).map { it?.habit }
    }

    suspend fun saveHabit(
        habitId: Long?,
        name: String,
        category: String
    ) {
        val trimmedName = name.trim()
        val trimmedCategory = category.trim().ifBlank { "General" }

        if (trimmedName.isBlank()) return

        if (habitId == null) {
            habitDao.insertHabit(
                HabitEntity(
                    name = trimmedName,
                    category = trimmedCategory,
                    startDate = todayString()
                )
            )
        } else {
            val existingHabit = habitDao.getHabitEntityById(habitId) ?: return
            habitDao.updateHabit(
                existingHabit.copy(
                    name = trimmedName,
                    category = trimmedCategory
                )
            )
        }
    }

    suspend fun deleteHabit(habitId: Long) {
        val habit = habitDao.getHabitEntityById(habitId) ?: return
        habitDao.deleteHabit(habit)
    }

    suspend fun toggleToday(habitId: Long) {
        val today = todayString()
        val existingLog = habitDao.getDailyLogForDate(habitId, today)

        // Tapping the button a second time removes today's completion.
        if (existingLog != null && existingLog.completed) {
            habitDao.deleteDailyLogForDate(habitId, today)
        } else {
            habitDao.insertOrUpdateDailyLog(
                DailyLogEntity(
                    habitId = habitId,
                    date = today,
                    completed = true
                )
            )
        }
    }
}

private fun HabitWithLogs.toListItem(): HabitListItem {
    return HabitListItem(
        id = habit.id,
        name = habit.name,
        category = habit.category,
        doneToday = isDoneToday(logs),
        streak = calculateStreak(logs)
    )
}

private fun HabitWithLogs.toDetailItem(): HabitDetailItem {
    return HabitDetailItem(
        id = habit.id,
        name = habit.name,
        category = habit.category,
        startDate = formatDisplayDate(habit.startDate),
        doneToday = isDoneToday(logs),
        streak = calculateStreak(logs)
    )
}
