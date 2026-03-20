package dev.sethdegay.sequence.feature.home.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sethdegay.sequence.core.model.CalendarEvent
import dev.sethdegay.sequence.core.ui.CalendarEventList

@Composable
fun EventSheetContainer(events: List<CalendarEvent>?) {
    when {
        events == null -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
            ) {
                LoadingIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(64.dp),
                )
            }
        }

        else -> CalendarEventList(events)
    }
}