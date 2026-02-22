package dev.sethdegay.sequence.core.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

data class PreferenceError(
    val message: String,
    val linkText: String? = null,
    val actionUri: String? = null,
    val actionUriErrorMessage: String? = null,
)

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun PreferenceError(preferenceError: PreferenceError) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
            append("${preferenceError.message} ")
        }

        if (preferenceError.linkText != null && preferenceError.actionUri != null) {
            withLink(
                LinkAnnotation.Url(
                    url = preferenceError.actionUri,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                    linkInteractionListener = {
                        try {
                            uriHandler.openUri(preferenceError.actionUri)
                        } catch (_: IllegalArgumentException) {
                            val errorMessage = preferenceError.actionUriErrorMessage
                                ?: context.getString(R.string.preference_error_cannot_open_uri)
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
//                        val intent = Intent(
//                            Intent.ACTION_VIEW,
//                            preferenceError.actionUri.toUri(),
//                        )
//                        context.startActivity(intent)
                    }
                )
            ) {
                append(preferenceError.linkText)
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = Modifier.padding(top = 4.dp),
        style = MaterialTheme.typography.bodySmall,
    )
}