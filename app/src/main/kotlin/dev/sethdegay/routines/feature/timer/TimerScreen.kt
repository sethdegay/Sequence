package dev.sethdegay.routines.feature.timer

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.sethdegay.routines.feature.TestScreen

@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
    navigateUp: () -> Unit,
) {
    val id by viewModel.idFlow.collectAsState()
    TestScreen("Timer screen | ID: $id") {
        Button(onClick = navigateUp) {
            Text("Navigate up")
        }
    }
}