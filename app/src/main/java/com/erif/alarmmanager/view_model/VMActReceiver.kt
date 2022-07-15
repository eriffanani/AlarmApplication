package com.erif.alarmmanager.view_model

import android.view.animation.Animation
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.model.receiver.ModelActReceiver
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import java.text.SimpleDateFormat
import java.util.*

class VMActReceiver constructor(
    private val model: ModelActReceiver,
    private val dbAlarm: DatabaseAlarm?,
    private val animationUp: Animation
): ViewModel() {

    init {
        loadAlarm()
        model.currentTime = getCurrentTime()
    }

    private fun getCurrentTime(): String{
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(Date())
    }

    private fun loadAlarm() {
        val list: MutableList<ModelItemAlarm> = dbAlarm?.getAlarm() ?: ArrayList()
        list.forEach { item->
            val ringtoneUri = item.ringtoneUri
            ringtoneUri?.let {
                val title = item.title
                val desc = item.desc
                model.alarmTitle = title
                model.alarmDesc = desc
            }
        }
    }

    fun animationUp(): Animation {
        return animationUp
    }

}