package com.mahamud.smarthabits.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val startDate: String
)

// Room can fetch a habit together with all of its daily log entries.
data class HabitWithLogs(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val logs: List<DailyLogEntity>
)
