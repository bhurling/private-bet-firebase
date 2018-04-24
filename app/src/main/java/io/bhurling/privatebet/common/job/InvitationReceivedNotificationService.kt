package io.bhurling.privatebet.common.job

import android.app.Notification
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.PersistableBundle
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.notification.CHANNEL_LINKS
import io.bhurling.privatebet.common.notification.safeSetChannelId
import io.bhurling.privatebet.common.ui.CircleTransformation
import org.koin.inject
import java.lang.Exception

class InvitationReceivedNotificationService : JobService() {

    private val navigator: Navigator by inject()

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters): Boolean {
        Picasso.get()
                .load(params.extras.photoUrl)
                .transform(CircleTransformation())
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        // ignore
                    }

                    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
                        postNotification(params.extras.userId, params.extras.displayName)

                        jobFinished(params, false)
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        postNotification(params.extras.userId, params.extras.displayName, bitmap)

                        jobFinished(params, false)
                    }

                })

        return true
    }

    private fun postNotification(userId: String, displayName: String, bitmap: Bitmap? = null) {
        val notification = Notification.Builder(this)
                .safeSetChannelId(CHANNEL_LINKS)
                .setContentTitle("You have an invitation")
                .setContentText("$displayName wants to connect to you on Private Bet")
                .setContentIntent(navigator.makeHomeScreenIntent(this))
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_person_black_32dp)
                .setStyle(Notification.BigTextStyle())
                .addAction(
                        Notification.Action.Builder(
                                0,
                                "Accept",
                                navigator.makeAcceptInvitationIntent(this, userId)
                        ).build()
                )
                .build()

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .notify(makeNotificationId(userId), notification)
    }

    companion object {
        internal const val EXTRA_PHOTO_URL = "io.bhurling.privatebet.extras.PHOTO_URL"
        internal const val EXTRA_DISPLAY_NAME = "io.bhurling.privatebet.extras.DISPLAY_NAME"
        internal const val EXTRA_USER_ID = "io.bhurling.privatebet.extras.USER_ID"

        fun schedule(context: Context, userId: String, photoUrl: String, displayName: String) {
            val componentName = ComponentName(context, InvitationReceivedNotificationService::class.java)

            val job = JobInfo.Builder(makeJobId(userId), componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(PersistableBundle().apply {
                        this.userId = userId
                        this.displayName = displayName
                        this.photoUrl = photoUrl
                    })
                    .build()

            (context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(job)
        }

        private fun makeJobId(userId: String) = "Invitation_$userId".hashCode()
        private fun makeNotificationId(userId: String) = makeJobId(userId)
    }
}

private var PersistableBundle.userId
    get() = getString(InvitationReceivedNotificationService.EXTRA_USER_ID)!!
    set(value) {
        putString(InvitationReceivedNotificationService.EXTRA_USER_ID, value)
    }

private var PersistableBundle.displayName
    get() = getString(InvitationReceivedNotificationService.EXTRA_DISPLAY_NAME)!!
    set(value) {
        putString(InvitationReceivedNotificationService.EXTRA_DISPLAY_NAME, value)
    }

private var PersistableBundle.photoUrl
    get() = getString(InvitationReceivedNotificationService.EXTRA_PHOTO_URL)!!
    set(value) {
        putString(InvitationReceivedNotificationService.EXTRA_PHOTO_URL, value)
    }
