package com.mahamud.smarthabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.habittracker.ui.theme.HabitTrackerTheme
import com.mahamud.smarthabits.ui.navigation.Navigation
import com.mahamud.smarthabits.util.HabitNotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HabitNotificationHelper.createNotificationChannel(this)
        enableEdgeToEdge()

        setContent {
            HabitTrackerTheme(darkTheme = false) {
                Navigation()
            }
        }
    }
}
