package com.mahamud.smarthabits

import com.mahamud.smarthabits.data.local.entity.DailyLogEntity
import com.mahamud.smarthabits.util.calculateStreak
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class HabitDateUtilsTest {

    @Test
    fun calculateStreak_countsConsecutiveDaysIncludingToday() {
        val today = LocalDate.now()
        val logs = listOf(
            DailyLogEntity(habitId = 1, date = today.toString(), completed = true),
            DailyLogEntity(habitId = 1, date = today.minusDays(1).toString(), completed = true),
            DailyLogEntity(habitId = 1, date = today.minusDays(2).toString(), completed = true)
        )

        assertEquals(3, calculateStreak(logs))
    }

    @Test
    fun calculateStreak_returnsZeroAfterMissedDay() {
        val today = LocalDate.now()
        val logs = listOf(
            DailyLogEntity(habitId = 1, date = today.minusDays(2).toString(), completed = true)
        )

        assertEquals(0, calculateStreak(logs))
    }
}
