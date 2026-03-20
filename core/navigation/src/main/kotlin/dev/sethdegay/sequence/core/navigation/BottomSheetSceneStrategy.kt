package dev.sethdegay.sequence.core.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {
    companion object {
        private const val METADATA_KEY = "navigation_bottom_sheet_metadata"

        fun bottomSheetMetadata(
            properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
        ): Map<String, Any> = mapOf(METADATA_KEY to BottomSheetMetadata(properties))

        private fun <T : Any> NavEntry<T>.asBottomSheetMetadata(): BottomSheetMetadata? {
            return metadata[METADATA_KEY] as? BottomSheetMetadata
        }
    }

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val currentEntry = entries.lastOrNull() ?: return null
        val sheetMetadata = currentEntry.asBottomSheetMetadata() ?: return null
        @Suppress("UNCHECKED_CAST")
        return BottomSheetScene(
            key = currentEntry.contentKey as T,
            overlaidEntries = entries.dropLast(1),
            entry = currentEntry,
            properties = sheetMetadata.properties,
            onDismiss = onBack,
        )
    }

    private data class BottomSheetMetadata(val properties: ModalBottomSheetProperties)
}

@OptIn(ExperimentalMaterial3Api::class)
internal class BottomSheetScene<T : Any>(
    override val key: T,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val properties: ModalBottomSheetProperties,
    private val onDismiss: () -> Unit,
) : OverlayScene<T> {
    override val previousEntries: List<NavEntry<T>> = overlaidEntries
    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable () -> Unit = {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            properties = properties,
            content = { entry.Content() },
        )
    }
}