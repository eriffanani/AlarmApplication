package com.erif.alarmmanager.utils.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class AlarmServiceFromBooting : IntentService("Intent Service") {

    private lateinit var dbAlarm: DatabaseAlarm
    private lateinit var helper : MyHelper
    private val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())

    override fun onHandleIntent(intent: Intent?) {
        dbAlarm = DatabaseAlarm(this)
        helper = MyHelper(this)
        loadAndStartAlarm()
    }

    private fun loadAndStartAlarm() {
        val list = dbAlarm.getAlarmActive()
        list.forEach {
            item->
            val id = item.id
            val index = item.index
            val timeList = item.timeList
            val jsonArray = JSONArray(timeList)
            val size = jsonArray.length()
            var getTime = jsonArray[index].toString()
            var date = helper.stringToDate(getTime)
            var millis = helper.dateToMillis(date)

            if (size == 1){
                val newDate = format.parse(getNewDate())
                if (date.after(newDate))
                    startAlarm(id, millis)
            }else{
                val newDate = format.parse(getNewDate())
                for (i in 0 until size){
                    getTime = jsonArray[i].toString()
                    date = helper.stringToDate(getTime)
                    millis = helper.dateToMillis(date)
                    if (date.after(newDate)){
                        startAlarm(id, millis)
                        break
                    }
                }
            }
        }
    }

    private fun startAlarm(id: Int, millis: Long){
        helper.triggerAlarm(id, millis, 1, false)
    }

    private fun getNewDate(): String{
        return format.format(Date())
    }

}