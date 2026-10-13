package com.mahamud.smarthabits.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_logs",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["habitId"]),
        Index(value = ["habitId", "date"], unique = true)
    ]
)
// Each row represents whether a habit was completed on one specific date.
data class DailyLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: String,
    val completed: Boolean = true
)
