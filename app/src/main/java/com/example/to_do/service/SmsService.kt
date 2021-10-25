package com.example.to_do.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.to_do.MainActivity
import com.example.to_do.R
import com.example.to_do.data.models.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SmsService : Service() {
    private val CHANNEL_ID = "ForegroundServiceChannel"
    private val SMS = "android.provider.Telephony.SMS_RECEIVED"
    private var mDatabase: DatabaseReference? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            mDatabase = FirebaseDatabase.getInstance().getReference()
            if (intent.action.equals(SMS)) {
                val bundle = intent.extras
                val objects = bundle?.get("pdus") as Array<*>?
                val messages = arrayOfNulls<SmsMessage>(objects!!.size)
                for (i in 0..objects.size - 1) {
                    messages[i] = SmsMessage.createFromPdu(objects[i] as ByteArray)
                }
                mDatabase!!.child("messages").child(messages[0]!!.displayOriginatingAddress)
                    .child(UUID.randomUUID().toString())
                    .setValue(Message(messages[0]!!.messageBody))
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.apply {
            addAction(SMS)
            addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        }
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = setNotification(pendingIntent)
        startForeground(1, notification)
        return super.onStartCommand(intent, flags, startId)
    }


    private fun setNotification(pendingIntent: PendingIntent): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_circle)
            .setContentTitle("Warning")
            .setContentText("You have important job to do. Do not forget them!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setWhen(0)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setColor(ContextCompat.getColor(application,R.color.red))
            .build()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}