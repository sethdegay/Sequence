package dev.sethdegay.sequence.core.database

import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteException
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.sethdegay.sequence.core.database.dao.WorkspaceDao
import dev.sethdegay.sequence.core.database.model.WorkspaceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class WorkspacePreloadCallback(
    private val scope: CoroutineScope,
    private val workspaceDaoProvider: () -> WorkspaceDao,
) : RoomDatabase.Callback() {
    @OptIn(ExperimentalUuidApi::class)
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch(Dispatchers.IO) {
            runCatching {
                withTimeout(10.seconds) {
                    val dao = workspaceDaoProvider()
                    if (dao.getCount() == 0) {
                        val now = Clock.System.now()
                        val workspace = WorkspaceEntity(
                            id = Uuid.random().toHexDashString(),
                            title = "Default",
                            description = "The default workspace",
                            dateCreated = now,
                            dateModified = now,
                        )
                        dao.insert(workspace)
                    } else {
                        Log.d("PRELOAD", "Database already contains data; skipping preload.")
                    }
                }
            }.onFailure { e ->
                when (e) {
                    is SQLiteException -> Log.e("PRELOAD", "SQL error during preload", e)
                    is TimeoutCancellationException -> Log.e("PRELOAD", "Preload timed out", e)
                    else -> throw e
                }
            }
        }
    }
}