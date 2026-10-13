package com.mahamud.smarthabits.ui.screens.addedit

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mahamud.smarthabits.data.local.entity.HabitEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    habit: HabitEntity?,
    onBack: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    // When editing, we pre-fill the form with the current habit values.
    LaunchedEffect(habit?.id) {
        name = habit?.name.orEmpty()
        category = habit?.category.orEmpty()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (habit == null) "Add Habit" else "Edit Habit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Keep the habit name short and easy to recognise.",
                    modifier = Modifier.padding(18.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Habit name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { onSave(name, category) },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("Save Habit")
            }
        }
    }
}
