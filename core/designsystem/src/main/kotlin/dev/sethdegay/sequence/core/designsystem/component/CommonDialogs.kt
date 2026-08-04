package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.sethdegay.sequence.core.designsystem.R
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons

@Composable
fun DeleteConfirmationDialog(
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = SequenceIcons.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = { Text(text = stringResource(R.string.delete_dialog_title, itemName)) },
        text = {
            Text(
                text = stringResource(R.string.delete_dialog_message, itemName),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.delete_dialog_delete_action),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.delete_dialog_cancel_action)) }
        },
    )
}