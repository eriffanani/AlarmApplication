package com.erif.alarmmanager.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.R
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.model.receiver.ModelActReceiver
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class VMActReceiver(private val context: Context, private val model: ModelActReceiver): ViewModel() {

    private var dbAlarm: DatabaseAlarm = DatabaseAlarm(context)

    init {
        loadAlarm()
        model.currentTime = getCurrentTime()
    }

    private fun getCurrentTime(): String{
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(Date())
    }

    private fun loadAlarm() {
        val list: MutableList<ModelItemAlarm> = dbAlarm.getAlarm()
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

    fun animationUp(): Animation{
        return AnimationUtils.loadAnimation(context, R.anim.anim_slide_up)
    }

}