package dev.sethdegay.routines.feature.editor

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.sethdegay.routines.feature.TestScreen

@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
    navigateUp: () -> Unit,
) {
    val id by viewModel.idFlow.collectAsState()
    TestScreen("Editor screen | ID: $id") {
        Button(onClick = navigateUp) {
            Text("Navigate up")
        }
    }
}