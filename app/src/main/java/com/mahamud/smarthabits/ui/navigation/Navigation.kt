package com.mahamud.smarthabits.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mahamud.smarthabits.ui.screens.addedit.AddEditHabitScreen
import com.mahamud.smarthabits.ui.screens.detail.HabitDetailScreen
import com.mahamud.smarthabits.ui.screens.home.HomeScreen
import com.mahamud.smarthabits.ui.screens.home.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

sealed interface HabitRoute : NavKey

@Serializable
data object HomeRoute : HabitRoute

@Serializable
data object AddHabitRoute : HabitRoute

@Serializable
data class HabitDetailRoute(val habitId: Long) : HabitRoute

@Serializable
data class EditHabitRoute(val habitId: Long) : HabitRoute

@Composable
fun Navigation(
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val scope = rememberCoroutineScope()
    val homeUiState = homeViewModel.uiState.collectAsStateWithLifecycle().value

    // Navigation 3 works by owning a back stack of route keys.
    val backStack = rememberNavBackStack(HomeRoute)

    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        },
        entryProvider = entryProvider {
            entry<HomeRoute> {
                HomeScreen(
                    uiState = homeUiState,
                    onAddHabit = { backStack.add(AddHabitRoute) },
                    onHabitClick = { habitId ->
                        backStack.add(HabitDetailRoute(habitId))
                    },
                    onFilterSelected = homeViewModel::setFilter,
                    onToggleToday = { habitId ->
                        scope.launch {
                            homeViewModel.toggleToday(habitId)
                        }
                    }
                )
            }

            entry<AddHabitRoute> {
                AddEditHabitScreen(
                    habit = null,
                    onBack = {
                        backStack.removeAt(backStack.lastIndex)
                    },
                    onSave = { name, category ->
                        scope.launch {
                            homeViewModel.saveHabit(
                                habitId = null,
                                name = name,
                                category = category
                            )
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
                )
            }

            entry<EditHabitRoute> { route ->
                val habit = homeViewModel
                    .getHabitForEdit(route.habitId)
                    .collectAsStateWithLifecycle(initialValue = null)
                    .value

                AddEditHabitScreen(
                    habit = habit,
                    onBack = {
                        backStack.removeAt(backStack.lastIndex)
                    },
                    onSave = { name, category ->
                        scope.launch {
                            homeViewModel.saveHabit(
                                habitId = route.habitId,
                                name = name,
                                category = category
                            )
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
                )
            }

            entry<HabitDetailRoute> { route ->
                val detail = homeViewModel
                    .getHabitDetail(route.habitId)
                    .collectAsStateWithLifecycle(initialValue = null)
                    .value

                HabitDetailScreen(
                    detail = detail,
                    onBack = {
                        backStack.removeAt(backStack.lastIndex)
                    },
                    onEdit = {
                        backStack.add(EditHabitRoute(route.habitId))
                    },
                    onDelete = {
                        scope.launch {
                            homeViewModel.deleteHabit(route.habitId)
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                    onToggleToday = {
                        scope.launch {
                            homeViewModel.toggleToday(route.habitId)
                        }
                    }
                )
            }
        }
    )
}
