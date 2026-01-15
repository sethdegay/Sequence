package dev.sethdegay.routines.core.ui

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
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.designsystem.component.CardGroupItem
import dev.sethdegay.routines.core.designsystem.component.CountdownDisplay
import dev.sethdegay.routines.core.designsystem.icon.RoutinesIcons
import dev.sethdegay.routines.core.designsystem.util.asComposableIconButton
import dev.sethdegay.routines.core.model.Task
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReorderableCardGroup(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onTaskOrderChanged: (List<Task>) -> Unit,
    onTaskClick: (Task) -> Unit,
    headerContent: @Composable LazyItemScope.() -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onTaskOrderChanged(
            tasks.toMutableList().apply { add(to.index - 1, removeAt(from.index - 1)) })
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState,
    ) {
        item(content = headerContent)
        items(items = tasks, key = { it.id }) { task ->
            ReorderableItem(reorderableLazyListState, key = task.id) {
                ReorderableCardGroupItem(task, onTaskClick)
            }
        }
    }
}

@Composable
internal fun ReorderableCollectionItemScope.ReorderableCardGroupItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
) {
    CardGroupItem {
        Row(
            modifier = Modifier
                .clickable(onClick = { onTaskClick(task) })
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(modifier = Modifier.padding(start = 16.dp), text = task.title)
                CountdownDisplay(
                    duration = task.duration,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            RoutinesIcons.DragHandle.asComposableIconButton(
                modifier = with(this@ReorderableCardGroupItem) {
                    Modifier.draggableHandle()
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
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(id = "a", title = "Task A", duration = 30.seconds),
                Task(id = "b", title = "Task B", duration = 20.seconds),
                Task(id = "c", title = "Task C", duration = 10.seconds),
            )
        )
    }
    ReorderableCardGroup(
        modifier = Modifier,
        tasks = tasks,
        onTaskOrderChanged = { tasks = it },
        onTaskClick = {},
        headerContent = {},
    )
}