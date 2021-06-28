package com.erif.alarmmanager.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import com.erif.alarmmanager.utils.services.AlarmBroadcastReceiver
import java.text.SimpleDateFormat
import java.util.*

class MyHelper(private val context: Context) {

    private val dbAlarm: DatabaseAlarm = DatabaseAlarm(context)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun toast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Check Active Alarm
    fun isAlarmActive(id: Int): Boolean{
        val item = dbAlarm.getAlarm(id)
        val active = item.active
        return active == Constant.STATUS_ACTIVE
    }

    // Get Millis
    /*private fun getMillis(id: Int): Long{
        val millis: Long
        val currentTimeMillis = System.currentTimeMillis()
        val item = dbAlarm.getAlarm(id)
        val hourToMillis = hourToMillis(item.duration)
        millis = (currentTimeMillis + hourToMillis)
        return millis
    }*/

    // Update Active Alarm From Database
    fun updateActive(id: Int, active: Int){
        dbAlarm.update(id, active)
    }

    // Update Alarm Date Time From Database
    fun updateTimeList(id: Int, timeList: String){
        dbAlarm.updateTimeList(id, timeList)
    }

    // Update Count Alarm From Database
    fun updateIndex(id: Int, newIndex: Int){
        dbAlarm.updateIndex(id, newIndex)
    }

    // Delete Alarm From Database
    fun deleteAlarmFromDatabase(id: Int){
        dbAlarm.deleteAlarm(id)
    }

    // Delete Alarm From Database
    /*fun getCount(id: Int): Int{
        val item = dbAlarm.getAlarm(id)
        return item.count
    }*/
    /*fun getDateTime(id: Int): String?{
        val item = dbAlarm.getAlarm(id)
        val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val format2 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val dateTime = item.dateTime
        var result: String? = null
        dateTime?.let {
            val date = format.parse(it)
            result = format2.format(date)
        }
        return result
    }*/

    // Is Duration Updated
    fun isDurationUpdated(id: Int, newDuration: Int): Boolean{
        val item = dbAlarm.getAlarm(id)
        val duration = item.duration
        return newDuration != duration
    }

    // Trigger Alarm
    fun triggerAlarm(id: Int, millis: Long, duration: Int, withMessage: Boolean) {
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        alarmIntent.putExtra("id", id)
        val pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
        else
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
        if (withMessage)
            toast("Alarm diatur dalam $duration jam")
    }

    // Cancel Alarm
    fun cancelAlarm(id: Int, withMessage: Boolean) {
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        if (withMessage)
            toast("Alarm dibatalkan")
    }

    // Get Hour To Millis
    /*fun hourToMillis(hour: Int): Long{
        val currentTimeMillis = System.currentTimeMillis()
        val newInterval = (hour * 60 * 60 * 1000).toLong()
        //val newInterval = 30000
        return currentTimeMillis + newInterval
    }*/

    // Get Minutes To Millis
    /*fun minutesToMillis(minutes: Int): Long{
        val currentTimeMillis = System.currentTimeMillis()
        val newInterval = (minutes * 60 * 1000).toLong()
        //val newInterval = 15000
        return currentTimeMillis + newInterval
    }*/

    fun dateToMillis(date: Date): Long{
        val calendar = Calendar.getInstance()
        //calendar.timeInMillis = System.currentTimeMillis()
        calendar.time = date
        return calendar.timeInMillis
    }

    fun stringToDate(value: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val newDate: Date? = format.parse(value)
        val calendar = Calendar.getInstance()
        newDate?.let {
            calendar.time = it
        }
        return calendar.time
    }

    fun dateToString(hour: Int, minutes: Int): String{
        val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, hour)
        calendar.add(Calendar.MINUTE, minutes)
        return format.format(calendar.time)
    }

    fun getMillis(hour: Int, minutes: Int): Long{
        val dateString = dateToString(hour, minutes)
        val date = stringToDate(dateString)
        return dateToMillis(date)
    }

}