package com.mahamud.smarthabits.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mahamud.smarthabits.data.local.AppDatabase
import com.mahamud.smarthabits.data.local.entity.HabitEntity
import com.mahamud.smarthabits.data.repository.HabitDetailItem
import com.mahamud.smarthabits.data.repository.HabitListItem
import com.mahamud.smarthabits.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class HabitFilter(val label: String) {
    ALL("All"),
    DONE_TODAY("Done Today"),
    PENDING("Pending")
}

data class HomeUiState(
    val habits: List<HabitListItem> = emptyList(),
    val selectedFilter: HabitFilter = HabitFilter.ALL
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HabitRepository(
        AppDatabase.getDatabase(application).habitDao()
    )

    private val selectedFilter = MutableStateFlow(HabitFilter.ALL)

    // The screen state is built from database data plus the current filter choice.
    val uiState = combine(
        repository.getHabits(),
        selectedFilter
    ) { habits, filter ->
        HomeUiState(
            habits = when (filter) {
                HabitFilter.ALL -> habits
                HabitFilter.DONE_TODAY -> habits.filter { it.doneToday }
                HabitFilter.PENDING -> habits.filter { !it.doneToday }
            },
            selectedFilter = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun setFilter(filter: HabitFilter) {
        selectedFilter.value = filter
    }

    fun getHabitDetail(habitId: Long): Flow<HabitDetailItem?> {
        return repository.getHabitDetail(habitId)
    }

    fun getHabitForEdit(habitId: Long): Flow<HabitEntity?> {
        return repository.getHabitForEdit(habitId)
    }

    suspend fun saveHabit(habitId: Long?, name: String, category: String) {
        repository.saveHabit(habitId, name, category)
    }

    suspend fun deleteHabit(habitId: Long) {
        repository.deleteHabit(habitId)
    }

    suspend fun toggleToday(habitId: Long) {
        repository.toggleToday(habitId)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                HomeViewModel(application)
            }
        }
    }
}
