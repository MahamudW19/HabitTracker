package com.mahamud.smarthabits.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mahamud.smarthabits.util.HabitNotificationHelper

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val habitId = intent?.getLongExtra(HabitNotificationHelper.EXTRA_HABIT_ID, -1L) ?: -1L
        val habitName = intent?.getStringExtra(HabitNotificationHelper.EXTRA_HABIT_NAME).orEmpty()

        // The receiver turns the broadcast into a real notification.
        if (habitId > 0L && habitName.isNotBlank()) {
            HabitNotificationHelper.showNotification(
                context = context,
                habitId = habitId,
                habitName = habitName
            )
        }
    }
}
