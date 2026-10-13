package com.mahamud.smarthabits.util

import com.mahamud.smarthabits.data.local.entity.DailyLogEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

fun todayString(): String = LocalDate.now().toString()

fun formatDisplayDate(date: String): String {
    return runCatching {
        LocalDate.parse(date).format(displayFormatter)
    }.getOrDefault(date)
}

fun isDoneToday(logs: List<DailyLogEntity>): Boolean {
    return logs.any { it.date == todayString() && it.completed }
}

fun calculateStreak(logs: List<DailyLogEntity>): Int {
    // A streak is counted backwards from today, or from yesterday if today is still pending.
    val completedDates = logs
        .filter { it.completed }
        .map { LocalDate.parse(it.date) }
        .toSet()

    if (completedDates.isEmpty()) return 0

    var streak = 0
    var currentDate = if (completedDates.contains(LocalDate.now())) {
        LocalDate.now()
    } else {
        LocalDate.now().minusDays(1)
    }

    while (completedDates.contains(currentDate)) {
        streak++
        currentDate = currentDate.minusDays(1)
    }

    return streak
}
