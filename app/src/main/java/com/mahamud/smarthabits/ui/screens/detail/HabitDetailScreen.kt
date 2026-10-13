package com.mahamud.smarthabits.ui.screens.detail

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mahamud.smarthabits.data.repository.HabitDetailItem
import com.mahamud.smarthabits.util.HabitNotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    detail: HabitDetailItem?,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleToday: () -> Unit
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && detail != null) {
            HabitNotificationHelper.sendTestReminderBroadcast(
                context = context,
                habitId = detail.id,
                habitName = detail.name
            )
        } else if (!granted) {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(detail?.name ?: "Habit Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (detail == null) {
            Text(
                text = "Habit not found",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // This summary card gives the main habit information in one place.
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = detail.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text("Category: ${detail.category}")
                        Text("Start Date: ${detail.startDate}")
                        Text("Streak: ${detail.streak} day(s)")
                        Text(if (detail.doneToday) "Status: Done today" else "Status: Pending today")
                    }
                }

                Text(
                    text = "Quick actions",
                    style = MaterialTheme.typography.titleMedium
                )

                // The toggle button is linked to today's DailyLog entry.
                Button(
                    onClick = onToggleToday,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (detail.doneToday) "Undo Today" else "Mark Today Complete")
                }

                Button(
                    onClick = {
                        if (HabitNotificationHelper.hasNotificationPermission(context)) {
                            HabitNotificationHelper.sendTestReminderBroadcast(
                                context = context,
                                habitId = detail.id,
                                habitName = detail.name
                            )
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            HabitNotificationHelper.sendTestReminderBroadcast(
                                context = context,
                                habitId = detail.id,
                                habitName = detail.name
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reminder")
                }

                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Habit")
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Habit")
                }
            }
        }
    }
}
