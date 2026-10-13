package com.mahamud.smarthabits.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahamud.smarthabits.data.repository.HabitListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAddHabit: () -> Unit,
    onHabitClick: (Long) -> Unit,
    onFilterSelected: (HabitFilter) -> Unit,
    onToggleToday: (Long) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Smart Habit Tracker") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabit) {
                Icon(Icons.Default.Add, contentDescription = "Add habit")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // This card gives a quick explanation of what the home screen does.
                WelcomeCard(totalHabits = uiState.habits.size)
            }

            item {
                // The filter row keeps the main user actions easy to explain in a demo.
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HabitFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = filter == uiState.selectedFilter,
                            onClick = { onFilterSelected(filter) },
                            label = { Text(filter.label) }
                        )
                    }
                }
            }

            if (uiState.habits.isEmpty()) {
                item {
                    EmptyStateCard()
                }
            } else {
                items(uiState.habits, key = { it.id }) { habit ->
                    HabitRow(
                        habit = habit,
                        onClick = { onHabitClick(habit.id) },
                        onToggleToday = { onToggleToday(habit.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeCard(totalHabits: Int) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = if (totalHabits == 0) {
                    "Start by adding your first habit."
                } else {
                    "You currently have $totalHabits habits to manage."
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = "No habits yet. Tap + to add one.",
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun HabitRow(
    habit: HabitListItem,
    onClick: () -> Unit,
    onToggleToday: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = habit.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = if (habit.doneToday) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Icon(
                        imageVector = if (habit.doneToday) Icons.Default.CheckCircle else Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp),
                        tint = if (habit.doneToday) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Current streak: ${habit.streak} day(s)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (habit.doneToday) "Completed today" else "Still pending today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onToggleToday) {
                    Text(if (habit.doneToday) "Undo Today" else "Done Today")
                }
            }
        }
    }
}
