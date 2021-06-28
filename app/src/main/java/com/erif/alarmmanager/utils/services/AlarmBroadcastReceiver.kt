package com.erif.alarmmanager.utils.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.database.DatabaseAlarm

class AlarmBroadcastReceiver: BroadcastReceiver() {

    private var context: Context? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val serviceIntent = Intent(context, AlarmServiceFromBooting::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context?.startForegroundService(serviceIntent)
            }else{
                context?.startService(serviceIntent)
            }
        } else {
            val bundle = intent?.extras
            if (intent?.action.equals("STOP_ALARM"))
                stopRingtoneService()
            val id = bundle?.getInt("id")
            id?.let {
                if (it > 0)
                    loadAlarm(it)
            }
        }
    }

    // Start Ringtone Service
    private fun startRingtoneService(id: Int){
        val intent = Intent(Intent(context, AlarmRingtoneServices::class.java))
        intent.putExtra("id", id)
        context?.startService(intent)
    }

    // Stop Ringtone Service
    private fun stopRingtoneService(){
        context?.stopService(Intent(context, AlarmRingtoneServices::class.java))
    }

    // Load Alarm
    private fun loadAlarm(id: Int){
        val dbAlarm = context?.let { DatabaseAlarm(it) }
        dbAlarm?.let {
            val item = it.getAlarm(id)
            item.let { mItem->
                val title = mItem.title
                val desc = mItem.desc
                val active = mItem.active
                if (active == Constant.STATUS_ACTIVE)
                    if (title != null){
                        if (desc != null){
                            startRingtoneService(id)
                        }
                    }
            }
        }
    }

}