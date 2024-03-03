package io.hurling.privatebet.core.sync

import android.content.Context
import androidx.work.WorkManager

object Sync {
    fun syncProfile(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueProfileSyncWork()
        }
    }
}