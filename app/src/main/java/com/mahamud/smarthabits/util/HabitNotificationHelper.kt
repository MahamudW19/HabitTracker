package com.mahamud.smarthabits.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.mahamud.smarthabits.MainActivity
import com.mahamud.smarthabits.notifications.HabitReminderReceiver

object HabitNotificationHelper {
    const val CHANNEL_ID = "habit_reminders"
    const val EXTRA_HABIT_ID = "habit_id"
    const val EXTRA_HABIT_NAME = "habit_name"

    // Android 13+ requires runtime permission before we can show notifications.
    fun hasNotificationPermission(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Habit Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for habit reminders"
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun sendTestReminderBroadcast(
        context: Context,
        habitId: Long,
        habitName: String
    ) {
        // The detail screen sends a simple broadcast so the receiver can build the notification.
        val intent = Intent(context, HabitReminderReceiver::class.java).apply {
            putExtra(EXTRA_HABIT_ID, habitId)
            putExtra(EXTRA_HABIT_NAME, habitName)
        }
        context.sendBroadcast(intent)
    }

    fun showNotification(
        context: Context,
        habitId: Long,
        habitName: String
    ) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannel(context)

        val openAppIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            habitId.toInt(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Habit Reminder")
            .setContentText("Time to work on \"$habitName\"")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(habitId.toInt(), notification)
    }
}
