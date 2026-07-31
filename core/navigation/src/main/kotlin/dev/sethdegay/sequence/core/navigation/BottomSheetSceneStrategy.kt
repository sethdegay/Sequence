package dev.sethdegay.sequence.core.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {
    companion object {
        private object BottomSheetKey : NavMetadataKey<ModalBottomSheetProperties>

        fun bottomSheetMetadata(
            properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
        ): Map<String, Any> = metadata { put(BottomSheetKey, properties) }
    }

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val currentEntry = entries.lastOrNull() ?: return null
        val properties = currentEntry.metadata[BottomSheetKey] ?: return null
        @Suppress("UNCHECKED_CAST")
        return BottomSheetScene(
            key = currentEntry.contentKey as T,
            overlaidEntries = entries.dropLast(1),
            entry = currentEntry,
            properties = properties,
            onDismiss = onBack,
        )
    }
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
            sheetState = rememberBottomSheetState(
                initialValue = SheetValue.Hidden,
                enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
            ),
        )
    }
}