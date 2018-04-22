package io.bhurling.privatebet.common.job

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle

class InvitationReceivedNotificationService : JobService() {

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters): Boolean {
        // TODO Fetch image an post notification

        return false
    }

    companion object {
        private const val EXTRA_PHOTO_URL = "io.bhurling.privatebet.extras.PHOTO_URL"
        private const val EXTRA_DISPLAY_NAME = "io.bhurling.privatebet.extras.PHOTO_URL"
        private const val EXTRA_USER_ID = "io.bhurling.privatebet.extras.USER_ID"

        fun schedule(context: Context, userId: String, photoUrl: String, displayName: String) {
            val componentName = ComponentName(context, InvitationReceivedNotificationService::class.java)

            val job = JobInfo.Builder(0, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(PersistableBundle().apply {
                        putString(EXTRA_USER_ID, userId)
                        putString(EXTRA_DISPLAY_NAME, displayName)
                        putString(EXTRA_PHOTO_URL, photoUrl)
                    })
                    .build()

            (context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(job)
        }

    }
}