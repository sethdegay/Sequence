package dev.sethdegay.sequence.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.designsystem.component.CardGroupItem
import dev.sethdegay.sequence.core.designsystem.component.CountdownDisplay
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.asComposableIconButton
import dev.sethdegay.sequence.core.model.Step
import dev.sethdegay.sequence.core.ui.R.string
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReorderableCardGroup(
    modifier: Modifier = Modifier,
    steps: List<Step>,
    onStepOrderChanged: (List<Step>) -> Unit,
    onStepClick: (Step) -> Unit,
    headerContent: @Composable LazyItemScope.() -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onStepOrderChanged(
            steps.toMutableList().apply { add(to.index - 1, removeAt(from.index - 1)) })
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState,
    ) {
        item(content = headerContent)
        items(items = steps, key = { it.id }) { step ->
            ReorderableItem(reorderableLazyListState, key = step.id) {
                ReorderableCardGroupItem(step, onStepClick)
            }
        }
    }
}

@Composable
internal fun ReorderableCollectionItemScope.ReorderableCardGroupItem(
    step: Step,
    onStepClick: (Step) -> Unit,
) {
    CardGroupItem {
        Row(
            modifier = Modifier
                .clickable(onClick = { onStepClick(step) })
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(modifier = Modifier.padding(start = 16.dp), text = step.title)
                CountdownDisplay(
                    duration = step.duration,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            SequenceIcons.DragHandle.asComposableIconButton(
                modifier = with(this@ReorderableCardGroupItem) {
                    Modifier.Companion.draggableHandle()
                },
                onClick = {},
                contentDescription = stringResource(string.drag_handle_content_description),
                enableToolTip = false,
            ).invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReorderableCardGroupPreview() {
    var steps by remember {
        mutableStateOf(
            listOf(
                Step(id = "a", title = "Step 1", duration = 30.seconds),
                Step(id = "b", title = "Step 2", duration = 20.seconds),
                Step(id = "c", title = "Step 3", duration = 10.seconds),
            )
        )
    }
    ReorderableCardGroup(
        modifier = Modifier,
        steps = steps,
        onStepOrderChanged = { steps = it },
        onStepClick = {},
        headerContent = {},
    )
}