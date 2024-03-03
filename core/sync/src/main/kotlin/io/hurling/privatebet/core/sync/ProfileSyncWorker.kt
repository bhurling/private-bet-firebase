package io.hurling.privatebet.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.data.ProfilesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

private const val WORK_NAME = "ProfileSyncWork"

// TODO make sure hilt is used to create these workers

@HiltWorker
internal class ProfileSyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val profilesRepository: ProfilesRepository,
    private val auth: Auth
) : CoroutineWorker(
    appContext, workerParams
) {
    override suspend fun doWork(): Result {
        val authUser = auth.authUser.filterNotNull().first()

        profilesRepository.updateProfile(authUser.uid, authUser.displayName, authUser.photoUrl)

        return Result.success()
    }
}

fun WorkManager.enqueueProfileSyncWork() {
    enqueueUniqueWork(
        WORK_NAME,
        ExistingWorkPolicy.KEEP,
        OneTimeWorkRequestBuilder<ProfileSyncWorker>().build()
    )
}
