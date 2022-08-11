package com.example.realestatemanager.domain.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.ui.MainActivity

class PropertyNotificationService(
    private val context: Context
) {

    companion object {
        const val PROPERTY_CHANNEL_ID = "property_channel"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNewPropertyNotification(property: Property) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, PROPERTY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_house_24)
            .setContentTitle("There is a new property")
            .setContentText("A new ${property.type}, in ${property.city} at the price of $${property.price}")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }
}