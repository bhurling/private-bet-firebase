package io.bhurling.privatebet.common.notification.invitation

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.BaseBundle
import android.os.Bundle
import android.os.PersistableBundle
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.notification.CHANNEL_LINKS
import io.bhurling.privatebet.common.notification.safeSetChannelId
import io.bhurling.privatebet.common.ui.CircleTransformation
import io.bhurling.privatebet.friends.InvitationsInteractor
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
                        postNotification(params.extras)

                        jobFinished(params, false)
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        postNotification(params.extras, bitmap)

                        jobFinished(params, false)
                    }

                })

        return true
    }

    private fun postNotification(extras: PersistableBundle, bitmap: Bitmap? = null) {
        val notification = Notification.Builder(this)
                .safeSetChannelId(CHANNEL_LINKS)
                .setContentTitle(getString(R.string.notification_invitation_received_title))
                .setContentText(getString(R.string.notification_invitation_received_message, extras.displayName))
                .setContentIntent(navigator.makeFriendsScreenIntent(this))
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_person_black_32dp)
                .setStyle(Notification.BigTextStyle())
                .apply {
                    if (extras.isAccepted) {
                        addAction(makeAcceptedAction())
                    } else {
                        addAction(makeAcceptAction(extras))
                    }
                }
                .build()

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .notify(makeNotificationId(extras.userId), notification)
    }

    private fun makeAcceptedAction(): Notification.Action {
        return Notification.Action.Builder(
                0,
                getString(R.string.action_accepted),
                null
        ).build()
    }

    private fun makeAcceptAction(extras: PersistableBundle): Notification.Action {
        return Notification.Action.Builder(
                0,
                getString(R.string.action_accept),
                AcceptInvitationService.makePendingIntent(this, extras.toBundle())
        ).build()
    }

    companion object {
        internal const val EXTRA_PHOTO_URL = "io.bhurling.privatebet.extras.PHOTO_URL"
        internal const val EXTRA_DISPLAY_NAME = "io.bhurling.privatebet.extras.DISPLAY_NAME"
        internal const val EXTRA_USER_ID = "io.bhurling.privatebet.extras.USER_ID"
        internal const val EXTRA_IS_ACCEPTED = "io.bhurling.privatebet.extras.IS_ACCEPTED"

        fun schedule(context: Context, userId: String, photoUrl: String, displayName: String, isAccepted: Boolean = false) {
            val componentName = ComponentName(context, InvitationReceivedNotificationService::class.java)

            val job = JobInfo.Builder(makeJobId(userId), componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(PersistableBundle().apply {
                        this.userId = userId
                        this.displayName = displayName
                        this.photoUrl = photoUrl
                        this.isAccepted = isAccepted
                    })
                    .build()

            (context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(job)
        }

        private fun makeJobId(userId: String) = "Invitation_$userId".hashCode()
        private fun makeNotificationId(userId: String) = makeJobId(userId)
    }
}

class AcceptInvitationService : IntentService("AcceptInvitationService") {

    private val interactor: InvitationsInteractor by inject()

    override fun onHandleIntent(intent: Intent) {
        with(intent.extras) {
            interactor.accept(userId)

            InvitationReceivedNotificationService
                    .schedule(this@AcceptInvitationService, userId, photoUrl, displayName, true)
        }
    }

    companion object {
        fun makePendingIntent(context: Context, bundle: Bundle): PendingIntent {
            val intent = Intent(context, AcceptInvitationService::class.java)
                    .putExtras(bundle)

            return PendingIntent.getService(context, 0, intent, 0)
        }
    }
}

private fun PersistableBundle.toBundle(): Bundle {
    return Bundle().apply {
        this.userId = this@toBundle.userId
        this.displayName = this@toBundle.displayName
        this.photoUrl = this@toBundle.photoUrl
        this.isAccepted = this@toBundle.isAccepted
    }
}

private var BaseBundle.userId
    get() = getString(InvitationReceivedNotificationService.EXTRA_USER_ID)!!
    set(value) {
        putString(InvitationReceivedNotificationService.EXTRA_USER_ID, value)
    }

private var BaseBundle.displayName
    get() = getString(InvitationReceivedNotificationService.EXTRA_DISPLAY_NAME)!!
    set(value) {
        putString(InvitationReceivedNotificationService.EXTRA_DISPLAY_NAME, value)
    }

private var BaseBundle.photoUrl
    get() = getString(InvitationReceivedNotificationService.EXTRA_PHOTO_URL)!!
    set(value) {
        putString(InvitationReceivedNotificationService.EXTRA_PHOTO_URL, value)
    }

private var BaseBundle.isAccepted
    @SuppressLint("NewApi")
    get() = getBoolean(InvitationReceivedNotificationService.EXTRA_IS_ACCEPTED)
    @SuppressLint("NewApi")
    set(value) {
        putBoolean(InvitationReceivedNotificationService.EXTRA_IS_ACCEPTED, value)
    }