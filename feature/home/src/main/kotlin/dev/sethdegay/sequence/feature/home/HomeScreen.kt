package dev.sethdegay.sequence.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import dev.sethdegay.sequence.core.designsystem.component.Accordion
import dev.sethdegay.sequence.core.designsystem.component.CountdownDisplay
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIconButton
import dev.sethdegay.sequence.core.model.HeatMapLevel
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Step
import dev.sethdegay.sequence.core.ui.AccordionHeader
import dev.sethdegay.sequence.core.ui.CalendarEventsSheet
import dev.sethdegay.sequence.core.ui.HeatMapCalendar
import dev.sethdegay.sequence.feature.home.R.string
import java.time.LocalDate

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
                    Text(text = stringResource(string.top_app_bar_title))
                },
                actions = {
                    SequenceIcons.Settings.asComposableIconButton(
                        onClick = navigateToSettings,
                        contentDescription = stringResource(string.home_navigate_to_settings_content_description),
                    ).invoke()
                }
            )
        },
        floatingActionButton = {
            HomeFab(
                visible = !uiState.showLoadingScreen() && uiState.accordionExpandedId == null,
                text = stringResource(string.home_add_sequence_button_text),
                onClick = { navigateToEditor(null) },
            )
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
                setAccordionExpandedId = { viewModel.setAccordionExpandedId(it) },
                isExpanded = { it == uiState.accordionExpandedId },
                sequences = uiState.sequences,
                heatMapData = uiState.heatMapData,
                heatMapCalendarStart = uiState.heatMapCalendarStart,
                heatMapCalendarEnd = uiState.heatMapCalendarEnd,
                onDateClicked = viewModel::showCalendarEventsSheet,
            )

            if (uiState.showCalendarEventsSheet) {
                CalendarEventsSheet(
                    calendarEvents = uiState.activeCalendarEvents,
                    onDismissRequest = { viewModel.showCalendarEventsSheet(null) },
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    scaffoldPadding: PaddingValues,
    navigateToEditor: (String?) -> Unit,
    navigateToTimer: (String) -> Unit,
    setAccordionExpandedId: (String?) -> Unit,
    isExpanded: (String) -> Boolean,
    sequences: List<Sequence>,
    heatMapData: Map<LocalDate, HeatMapLevel>,
    heatMapCalendarStart: LocalDate,
    heatMapCalendarEnd: LocalDate,
    onDateClicked: (LocalDate) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .fillMaxSize(),
        contentPadding = scaffoldPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            HeatMapCalendar(
                modifier = Modifier.padding(16.dp),
                heatMapData = heatMapData,
                start = heatMapCalendarStart,
                end = heatMapCalendarEnd,
                onDateClicked = onDateClicked,
            )
        }
        items(sequences) { sequence ->
            val isExpanded = isExpanded(sequence.id)
            Accordion(
                isExpanded = isExpanded,
                header = { contentPadding ->
                    AccordionHeader(
                        modifier = Modifier.fillMaxWidth(),
                        isExpanded = isExpanded,
                        onClick = { isExpanded ->
                            setAccordionExpandedId(
                                if (isExpanded) {
                                    sequence.id
                                } else {
                                    null
                                }
                            )
                        },
                        onLongClick = { navigateToEditor(sequence.id) },
                        onPlayButtonClick = { navigateToTimer(sequence.id) },
                        title = sequence.title,
                        description = sequence.description,
                        totalDuration = sequence.totalDuration,
                        padding = contentPadding,
                    )
                }
            ) {
                sequence.steps.forEach { step ->
                    item { contentPadding ->
                        SequenceStep(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(contentPadding),
                            step,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeFab(
    modifier: Modifier = Modifier,
    visible: Boolean,
    text: String,
    onClick: () -> Unit,
    animationDurationMillis: Int = 200,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(animationDurationMillis)) +
                scaleIn(animationSpec = tween(animationDurationMillis)),
        exit = scaleOut(animationSpec = tween(animationDurationMillis)) +
                fadeOut(animationSpec = tween(animationDurationMillis)),
    ) {
        MediumExtendedFloatingActionButton(
            modifier = modifier,
            onClick = onClick,
        ) {
            Text(text = text)
        }
    }
}

@Composable
private fun SequenceStep(modifier: Modifier, step: Step) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = step.title)
        CountdownDisplay(
            duration = step.duration,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}