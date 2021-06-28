package com.erif.alarmmanager.utils.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.erif.alarmmanager.R
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import com.erif.alarmmanager.view.receiver.ActReceiver
import org.json.JSONArray

class AlarmRingtoneServices: Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var vibrator: Vibrator? = null
    private val vibratePattern = longArrayOf( 0, 500, 500)
    private lateinit var helper: MyHelper
    private var handler: Handler? = null

    companion object{
        const val CHANNEL_ID = "alarm_channel_001"
        const val CHANNEL_NAME = "Alarm Channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        helper = MyHelper(this)
        val bundle = intent?.extras
        bundle?.let {
            val id = it.getInt("id")
            loadAlarm(id)
        }
        return START_STICKY
    }

    // Load Alarm
    private fun loadAlarm(id: Int){
        val dbAlarm = DatabaseAlarm(this)
        dbAlarm.let {
            val item = it.getAlarm(id)
            item.let { mItem->
                val active = mItem.active
                if (active == Constant.STATUS_ACTIVE){
                    val title = mItem.title
                    title?.let {
                        mTitle->
                        val desc = mItem.desc
                        desc?.let {
                            mDesc->
                            showNotification(mTitle, mDesc)
                        }
                    }
                    val getVibrate = mItem.vibrate
                    if (getVibrate == Constant.STATUS_ACTIVE)
                        vibrate()
                    val ringtoneUri = mItem.ringtoneUri
                    ringtoneUri?.let {
                        mUri->
                        playRingtone(Uri.parse(mUri))
                    }
                    val index = item.index
                    val timeList = item.timeList
                    val jsonArray = JSONArray(timeList)
                    val size = jsonArray.length()
                    if (size == 1){
                        Log.e("Alarm", "Clear")
                        helper.updateActive(id, Constant.STATUS_NON_ACTIVE)
                    }else{
                        if (index < (size-1)){
                            val newIndex = index+1
                            val getTime = jsonArray[newIndex].toString()
                            val date = helper.stringToDate(getTime)
                            val millis = helper.dateToMillis(date)
                            setNewAlarm(id, millis)
                            helper.updateIndex(id, newIndex)
                            Log.e("Alarm", "Trigger New Alarm")
                        }else{
                            Log.e("Alarm", "Clear")
                            helper.updateActive(id, Constant.STATUS_NON_ACTIVE)
                        }
                    }
                }
            }
        }
    }

    private fun setNewAlarm(id: Int, millis: Long){
        helper.triggerAlarm(id, millis, 1, false)
    }

    // Play Ringtone
    private fun playRingtone(mUri: Uri){
        mediaPlayer = MediaPlayer.create(this, mUri)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    // Show Notification
    private fun showNotification(title: String, desc: String){
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.vibrationPattern = vibratePattern
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            /*val attr = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_RING)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            val defaultSound = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.alarm_manager_default)
            channel.setSound(defaultSound, attr)*/
            mNotificationManager.createNotificationChannel(channel)
            mBuilder = NotificationCompat.Builder(this, channel.id)
        }else{
            @Suppress("DEPRECATION")
            mBuilder = NotificationCompat.Builder(this)
        }
        val fullScreenIntent = Intent(this, ActReceiver::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
                this, 0,
                fullScreenIntent, PendingIntent.FLAG_ONE_SHOT
        )


        /*val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText("BIG")
        bigText.setBigContentTitle("Today's Bible Verse")
        bigText.setSummaryText("Text in detail")*/

        //mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.ic_notification)
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(desc)
        mBuilder.setOngoing(true)
        mBuilder.setAutoCancel(true)
        mBuilder.priority = NotificationCompat.PRIORITY_HIGH
        mBuilder.setCategory(NotificationCompat.CATEGORY_CALL)
        mBuilder.setFullScreenIntent(fullScreenPendingIntent, true)
        //mBuilder.setStyle(bigText)
        mBuilder.setVibrate(vibratePattern)
        //val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        /*val defaultSound = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.alarm_manager_default)
        mBuilder.setSound(defaultSound, AudioManager.STREAM_MUSIC)*/
        val intentStop = Intent(this, AlarmBroadcastReceiver::class.java)
        intentStop.action = "STOP_ALARM"
        val pendingStop = PendingIntent.getBroadcast(this, 0, intentStop, 0)
        mBuilder.addAction(R.drawable.ic_notification, "Hentikan alarm", pendingStop)

        //mNotificationManager.notify(CHANNEL_ID_NOTIFICATION, mBuilder.build())
        startForeground(NOTIFICATION_ID, mBuilder.build())
    }

    // Vibrate
    private fun vibrate(){
        handler = Handler()
        val monitor = Runnable {
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vibrator?.vibrate(VibrationEffect.createWaveform(vibratePattern, 0))
            else
                @Suppress("DEPRECATION")
                vibrator?.vibrate(vibratePattern, 0)
        }
        handler?.postDelayed(monitor, 1000)
    }

    // On Destroy
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator?.cancel()
        handler?.removeCallbacksAndMessages(null)
    }

    /*private fun dismissNotification(){
        mNotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager?.cancel(CHANNEL_ID_NOTIFICATION)
    }*/

}