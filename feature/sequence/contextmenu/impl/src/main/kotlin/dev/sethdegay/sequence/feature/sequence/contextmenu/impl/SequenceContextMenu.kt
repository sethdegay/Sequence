package dev.sethdegay.sequence.feature.sequence.contextmenu.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.common.toDateTimeString
import dev.sethdegay.sequence.core.designsystem.component.ContextAction
import dev.sethdegay.sequence.core.designsystem.component.ContextActionsRow
import dev.sethdegay.sequence.core.designsystem.component.LoadingSection
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun SequenceContextMenu(
    viewModel: SequenceContextMenuViewModel,
    navigateUp: () -> Unit,
    editSequence: (Uuid, Uuid) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val uiState = uiState) {
        is SequenceContextMenuUiState.Loading -> LoadingSection()
        is SequenceContextMenuUiState.Success -> SequenceContextMenu(
            title = uiState.title,
            dateCreated = uiState.dateCreated,
            dateModified = uiState.dateModified,
            actions = listOf(
                ContextAction(
                    label = stringResource(id = R.string.context_action_edit),
                    icon = SequenceIcons.Edit,
                    onClick = {
                        val (sequenceId, libraryId) = viewModel.getIds()
                        editSequence(sequenceId, libraryId)
                    },
                ),
                ContextAction(
                    label = stringResource(id = R.string.context_action_duplicate),
                    icon = SequenceIcons.Duplicate,
                    onClick = viewModel::duplicate,
                ),
                ContextAction(
                    label = stringResource(id = R.string.context_action_delete),
                    icon = SequenceIcons.Delete,
                    onClick = viewModel::delete,
                    isDangerous = true,
                ),
            ),
        )
    }

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect {
            when (it) {
                is SequenceContextMenuEffect.Finished -> navigateUp()
            }
        }
    }
}

@Composable
private fun SequenceContextMenu(
    modifier: Modifier = Modifier,
    title: String,
    dateCreated: Instant,
    dateModified: Instant,
    actions: List<ContextAction>,
) {
    LazyColumn(modifier = modifier) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(
                        R.string.date_created,
                        dateCreated.toDateTimeString(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = stringResource(
                        R.string.date_modified,
                        dateModified.toDateTimeString(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        item {
            ContextActionsRow(actions)
        }
    }
}
