package dev.sethdegay.sequence.feature.timer.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import dev.sethdegay.sequence.core.designsystem.R.string.navigate_up_content_description
import dev.sethdegay.sequence.core.designsystem.component.CountdownDisplay
import dev.sethdegay.sequence.core.designsystem.component.LoadingScreen
import dev.sethdegay.sequence.core.designsystem.component.ProgressIndicator
import dev.sethdegay.sequence.core.designsystem.component.TimerControls
import dev.sethdegay.sequence.core.designsystem.component.TimerControlsActions
import dev.sethdegay.sequence.core.designsystem.component.TimerControlsMode
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIconButton

@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val isTopAppBarExpanded by remember {
        derivedStateOf { scrollBehavior.state.collapsedFraction == 0f }
    }

    val shouldNavigateUp = uiState.shouldNavigateUp()
    LaunchedEffect(shouldNavigateUp) {
        if (shouldNavigateUp) {
            navigateUp()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = "")
                },
                navigationIcon = SequenceIcons.NavigateUp.asComposableIconButton(
                    onClick = dropUnlessResumed { navigateUp() },
                    contentDescription = stringResource(navigate_up_content_description),
                ),
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        }
    ) { padding ->
        if (uiState.showLoadingScreen()) {
            LoadingScreen(modifier = Modifier.padding(padding))
        } else {
            when (uiState) {
                is TimerUiState.Success -> TimerScreen(
                    scaffoldPadding = padding,
                    uiState = uiState as TimerUiState.Success,
                    expandProgressIndicator = isTopAppBarExpanded,
                    actions = viewModel,
                )

                else -> Text(
                    modifier = Modifier.padding(padding),
                    text = uiState.toString(),
                )
            }
        }
    }
}

@Composable
private fun TimerScreen(
    scaffoldPadding: PaddingValues,
    uiState: TimerUiState.Success,
    expandProgressIndicator: Boolean,
    actions: TimerControlsActions,
) {
    Box(
        modifier = Modifier
            .consumeWindowInsets(scaffoldPadding)
            .padding(scaffoldPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        ProgressIndicator(
            progress = uiState.progress,
            expanded = expandProgressIndicator,
            amplitudeLevel = uiState.amplitudeLevel,
        )
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = uiState.currentStep.title)
            CountdownDisplay(duration = uiState.remainingTime)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        ) {
            TimerControls(
                mode = if (uiState.isTimerRunning) TimerControlsMode.RUNNING else TimerControlsMode.PAUSED,
                actions = actions,
                canMovePrevious = uiState.canMovePrevious,
                canMoveNext = uiState.canMoveNext,
            )
        }
    }
}