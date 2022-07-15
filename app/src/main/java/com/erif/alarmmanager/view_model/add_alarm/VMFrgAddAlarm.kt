package com.erif.alarmmanager.view_model.add_alarm

import android.widget.CompoundButton
import android.widget.NumberPicker
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.model.add_alarm.ModelFrgAddAlarm
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import org.json.JSONArray

class VMFrgAddAlarm constructor(
    private val model: ModelFrgAddAlarm,
    private val dbAlarm: DatabaseAlarm?,
    private val helper: MyHelper?
): ViewModel() {

    private val liveClick = MutableLiveData<Any>()

    init {
        if (model.update)
            loadAlarm(model.alarmId)
    }

    private fun loadAlarm(alarmId: Int) {
        val itemAlarm = dbAlarm?.getAlarm(alarmId)
        model.alarmTitle = itemAlarm?.title
        model.alarmDesc = itemAlarm?.desc
        model.ringtoneTitle = itemAlarm?.ringtoneTitle
        model.ringtoneUri = itemAlarm?.ringtoneUri
        model.alarmDuration = itemAlarm?.duration ?: 0
        model.vibrate = itemAlarm?.vibrate ?: 0
    }

    fun onClickRingtone() {
        liveClick.value = 1
    }

    fun onClickTitle() {
        liveClick.value = 2
    }

    fun onClickDesc() {
        liveClick.value = 3
    }

    fun onClickVibrate() {
        model.vibrate = if (model.vibrate == Constant.STATUS_ACTIVE) {
                    Constant.STATUS_NON_ACTIVE
        } else {
            Constant.STATUS_ACTIVE
        }
    }

    fun onClickSave() {
        val item = ModelItemAlarm()
        item.title = model.alarmTitle
        item.desc = model.alarmDesc
        item.duration = model.alarmDuration
        item.ringtoneTitle = model.ringtoneTitle
        item.ringtoneUri = model.ringtoneUri
        item.vibrate = model.vibrate

        val jsonArray = JSONArray()
        if (model.alarmDuration > 1){
            val lastIndex = model.alarmDuration - 1
            var added = 0
            for (i in 0 until lastIndex){
                added = i+1
                val dateString = helper?.dateToString(added, 0)
                jsonArray.put(dateString)
            }
            var addMin = 0
            for (i in 0..3){
                addMin+=15
                val dateString = helper?.dateToString(added, addMin)
                jsonArray.put(dateString)
            }
        }else{
            val dateString = helper?.dateToString(1, 0)
            jsonArray.put(dateString)
        }
        item.timeList = jsonArray.toString()
        liveClick.value = item
    }

    fun switchListener(): CompoundButton.OnCheckedChangeListener{
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            model.vibrate = if(isChecked) Constant.STATUS_ACTIVE else Constant.STATUS_NON_ACTIVE
        }
    }

    fun pickerListener(): NumberPicker.OnValueChangeListener{
        return NumberPicker.OnValueChangeListener { _, _, newVal ->
            model.alarmDuration = newVal
        }
    }

    fun mutableClick(): MutableLiveData<Any> {
        return liveClick
    }

}