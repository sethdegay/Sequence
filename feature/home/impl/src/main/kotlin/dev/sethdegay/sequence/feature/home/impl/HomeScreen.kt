package dev.sethdegay.sequence.feature.home.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import dev.sethdegay.sequence.core.designsystem.component.DropdownButton
import dev.sethdegay.sequence.core.designsystem.component.DurationDisplay
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.Icon
import dev.sethdegay.sequence.core.designsystem.util.IconButton
import dev.sethdegay.sequence.core.model.HeatMapLevel
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.ui.AccordionHeader
import dev.sethdegay.sequence.core.ui.HeatMapCalendar
import dev.sethdegay.sequence.feature.home.impl.R.string
import java.time.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToEventList: (ClosedRange<Instant>) -> Unit,
    createSequence: (Uuid) -> Unit,
    navigateToSequenceContextMenu: (Uuid, Uuid) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToTimer: (Uuid) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    DropdownButton(
                        text = uiState.libraryTitle,
                        onClick = {},
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    SequenceIcons.Settings.IconButton(
                        onClick = navigateToSettings,
                        contentDescription = stringResource(string.home_navigate_to_settings_content_description),
                    )
                }
            )
        },
        floatingActionButton = {
            HomeFab(
                visible = !uiState.showLoadingScreen() && uiState.activeSequenceId == null,
                text = stringResource(string.home_add_sequence_button_text),
                onClick = { viewModel.getLibraryId()?.let { createSequence(it) } },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        if (uiState.showLoadingScreen()) {
            LoadingScreen(modifier = Modifier.padding(padding))
        } else {
            HomeScreen(
                scaffoldPadding = padding,
                navigateToEditor = { sequenceId ->
                    viewModel.getLibraryId()?.let { libraryId ->
                        navigateToSequenceContextMenu(sequenceId, libraryId)
                    }
                },
                navigateToTimer = navigateToTimer,
                setActiveSequenceId = viewModel::setActiveSequenceId,
                isExpanded = { it == uiState.activeSequenceId },
                sequences = uiState.sequences,
                heatMapData = uiState.heatMapData,
                heatMapCalendarStart = uiState.heatMapCalendarStart,
                heatMapCalendarEnd = uiState.heatMapCalendarEnd,
                onDateClicked = { navigateToEventList(viewModel.onCalendarDateSelected(it)) },
            )
        }
    }
}

@Composable
private fun HomeScreen(
    scaffoldPadding: PaddingValues,
    navigateToEditor: (Uuid) -> Unit,
    navigateToTimer: (Uuid) -> Unit,
    setActiveSequenceId: (Uuid?) -> Unit,
    isExpanded: (Uuid) -> Boolean,
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
                header = {
                    AccordionHeader(
                        modifier = Modifier.fillMaxWidth(),
                        isExpanded = isExpanded,
                        onClick = { setActiveSequenceId(if (it) sequence.id else null) },
                        onLongClick = { navigateToEditor(sequence.id) },
                        onPlayButtonClick = { navigateToTimer(sequence.id) },
                        sequence = sequence,
                    )
                }
            ) {
                sequence.segments.forEach { segment ->
                    item { contentPadding ->
                        SequenceSegment(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(contentPadding),
                            segment,
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
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ) {
            SequenceIcons.Add.Icon()
            Spacer(Modifier.size(12.dp))
            Text(text = text)
        }
    }
}

@Composable
private fun SequenceSegment(modifier: Modifier, segment: Segment) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = segment.title)
        DurationDisplay(
            duration = segment.duration,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}