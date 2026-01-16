package dev.sethdegay.routines.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.Accordion
import dev.sethdegay.routines.core.designsystem.component.CountdownDisplay
import dev.sethdegay.routines.core.designsystem.component.LoadingScreen
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIconButton
import dev.sethdegay.routines.core.model.Routine
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.ui.RoutineAccordionHeader

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToEditor: (String?) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToTimer: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(string.app_name))
                },
                actions = {
                    RoutinesIcons.Settings.asComposableIconButton(
                        onClick = navigateToSettings,
                        contentDescription = stringResource(string.home_navigate_to_settings_content_description),
                    ).invoke()
                }
            )
        },
        floatingActionButton = {
            if (!uiState.showLoadingScreen() && uiState.routinesAccordionExpandedId == null) {
                MediumExtendedFloatingActionButton(
                    onClick = { navigateToEditor(null) }
                ) {
                    Text(text = stringResource(string.home_add_routine_button_text))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        if (uiState.showLoadingScreen()) {
            LoadingScreen(modifier = Modifier.padding(padding))
        } else {
            HomeScreen(
                scaffoldPadding = padding,
                navigateToEditor = navigateToEditor,
                navigateToTimer = navigateToTimer,
                setRoutinesAccordionExpandedId = { viewModel.setRoutinesAccordionExpandedId(it) },
                isExpanded = { it == uiState.routinesAccordionExpandedId },
                routines = uiState.routines,
            )
        }
    }
}

@Composable
private fun HomeScreen(
    scaffoldPadding: PaddingValues,
    navigateToEditor: (String?) -> Unit,
    navigateToTimer: (String) -> Unit,
    setRoutinesAccordionExpandedId: (String?) -> Unit,
    isExpanded: (String) -> Boolean,
    routines: List<Routine>,
) {
    LazyColumn(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .fillMaxSize(),
        contentPadding = scaffoldPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(routines) { routine ->
            val isExpanded = isExpanded(routine.id)
            Accordion(
                isExpanded = isExpanded,
                header = { contentPadding ->
                    RoutineAccordionHeader(
                        modifier = Modifier.fillMaxWidth(),
                        isExpanded = isExpanded,
                        onClick = { isExpanded ->
                            setRoutinesAccordionExpandedId(
                                if (isExpanded) {
                                    routine.id
                                } else {
                                    null
                                }
                            )
                        },
                        onLongClick = { navigateToEditor(routine.id) },
                        onPlayButtonClick = { navigateToTimer(routine.id) },
                        title = routine.title,
                        description = routine.description,
                        padding = contentPadding,
                    )
                }
            ) {
                routine.tasks.forEach { task ->
                    item { contentPadding ->
                        RoutineTask(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(contentPadding),
                            task,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineTask(modifier: Modifier, task: Task) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = task.title)
        CountdownDisplay(
            duration = task.duration,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}