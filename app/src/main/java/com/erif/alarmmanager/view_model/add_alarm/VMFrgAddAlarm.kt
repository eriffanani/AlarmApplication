package com.erif.alarmmanager.view_model.add_alarm

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.CompoundButton
import android.widget.NumberPicker
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.utils.callback.CallbackAlarmForm
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.model.add_alarm.ModelFrgAddAlarm
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.view.add_alarm.form.ActAddDesc
import com.erif.alarmmanager.view.add_alarm.form.ActAddTitle
import com.erif.alarmmanager.view.add_alarm.form.ringtone_chooser.ActRingtoneChooser
import org.json.JSONArray
import java.util.*

@SuppressLint("StaticFieldLeak")
class VMFrgAddAlarm constructor(
    private val context: Context,
    private val model: ModelFrgAddAlarm,
    private val dialog: Dialog,
    private val callbackForm: CallbackAlarmForm
): ViewModel() {

    private var dbAlarm: DatabaseAlarm = DatabaseAlarm(context)
    private var activity: Activity = context as Activity
    private val helper = MyHelper(context)

    init {
        if (model.update)
            loadAlarm(model.alarmId)
    }

    private fun loadAlarm(alarmId: Int) {
        val itemAlarm = dbAlarm.getAlarm(alarmId)
        model.alarmTitle = itemAlarm.title
        model.alarmDesc = itemAlarm.desc
        model.ringtoneTitle = itemAlarm.ringtoneTitle
        model.ringtoneUri = itemAlarm.ringtoneUri
        model.alarmDuration = itemAlarm.duration
        model.vibrate = itemAlarm.vibrate
    }

    fun onClickClose(){
        closeDialog()
    }

    fun onClickRingtone(){
        val intent = Intent(context, ActRingtoneChooser::class.java)
        activity.startActivityForResult(intent, Constant.REQUEST_CODE_RINGTONE)
    }

    fun onClickTitle(){
        val intent = Intent(activity, ActAddTitle::class.java)
        intent.putExtra("alarmTitle", model.alarmTitle)
        activity.startActivityForResult(intent, Constant.REQUEST_CODE_TITLE)
    }

    fun onClickDesc(){
        val intent = Intent(context, ActAddDesc::class.java)
        intent.putExtra("alarmDesc", model.alarmDesc)
        activity.startActivityForResult(intent, Constant.REQUEST_CODE_DESC)
    }

    fun onClickVibrate(){
        model.vibrate =
                when(model.vibrate){
                    Constant.STATUS_ACTIVE ->{
                        Constant.STATUS_NON_ACTIVE
                    }
                    else ->{
                        Constant.STATUS_ACTIVE
                    }
                }
    }

    fun onClickSave(){
        saveAlarm()
        closeDialog()
    }

    private fun saveAlarm(){
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
                val dateString = helper.dateToString(added, 0)
                jsonArray.put(dateString)
            }
            var addMin = 0
            for (i in 0..3){
                addMin+=15
                val dateString = helper.dateToString(added, addMin)
                jsonArray.put(dateString)
            }
        }else{
            val dateString = helper.dateToString(1, 0)
            jsonArray.put(dateString)
        }

        item.timeList = jsonArray.toString()
        if (model.update) {
            model.alarmId.let {
                id->
                val getIsDurationUpdated = helper.isDurationUpdated(id, model.alarmDuration)
                dbAlarm.update(id, item)
                callbackForm.onUpdate(id, getIsDurationUpdated, model.alarmDuration)
            }
        }else{
            dbAlarm.insert(item)
            val id: Int = dbAlarm.getLastID()
            callbackForm.onInsert(id, model.alarmDuration)
        }
    }

    private fun closeDialog(){
        dialog.dismiss()
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

}