package io.bhurling.privatebet.common.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import io.bhurling.privatebet.R

const val CHANNEL_LINKS = "LINKS"

fun createChannels(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    with(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
        NotificationChannel(
                CHANNEL_LINKS,
                context.getString(R.string.channel_name_links),
                NotificationManager.IMPORTANCE_DEFAULT
        ).let {
            createNotificationChannel(it)
        }
    }
}

fun Notification.Builder.safeSetChannelId(channelId: String): Notification.Builder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        setChannelId(channelId)
    } else {
        this
    }
}
