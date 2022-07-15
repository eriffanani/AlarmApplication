package com.erif.alarmmanager.view_model

import android.os.SystemClock
import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.model.ModelActMain
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.adapter.AdapterAlarmList
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import org.json.JSONArray

class VMActMain constructor(
    private val model: ModelActMain,
    private val dbAlarm: DatabaseAlarm?,
    private val helper: MyHelper?
) : ViewModel() {

    private val adapter: AdapterAlarmList = AdapterAlarmList(this)
    var list: MutableList<ModelItemAlarm> = ArrayList()
    var currentID: Int = -1
    var currentPosition: Int = -1
    private var mLastClickTime: Long = 0

    private val liveClick = MutableLiveData<Any>()

    // Setup Alarm List
    fun setupAlarmList(){
        list = dbAlarm?.getAlarm() ?: ArrayList()
        if (list.size < 1) {
            showEmpty()
        }else{
            adapter.setList(list)
            hideEmpty()
        }
    }

    // On Click Fab
    fun onClickFabAdd(){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return
        mLastClickTime = SystemClock.elapsedRealtime()
        liveClick.value = 0
    }

    // Get Adapter
    fun getAdapter(): AdapterAlarmList {
        return adapter
    }

    // On Click Item
    fun onClickItem(item: ModelItemAlarm){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return
        mLastClickTime = SystemClock.elapsedRealtime()
        liveClick.value = item
    }

    // Switch Check Listener
    fun itemCheckListener(item: ModelItemAlarm): CompoundButton.OnCheckedChangeListener{
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            item.active = if(isChecked) Constant.STATUS_ACTIVE else Constant.STATUS_NON_ACTIVE
            helper?.updateActive(item.id, item.active)
            val millis = helper?.getMillis(1, 0) ?: 0
            if (isChecked) {
                helper?.triggerAlarm(item.id, millis, item.duration, true)

                val jsonArray = JSONArray()
                if (item.duration > 1){
                    val lastIndex = item.duration - 1
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
                helper?.updateTimeList(item.id, jsonArray.toString())
                helper?.updateIndex(item.id, 0)
            }else{
                helper?.cancelAlarm(item.id, true)
            }
        }
    }

    // On Click Item Delete
    fun onClickItemDelete(item: ModelItemAlarm){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return
        mLastClickTime = SystemClock.elapsedRealtime()
        currentID = item.id
        currentPosition = list.indexOf(item)
        liveClick.value = 1
    }

    // Show Empty List
    fun showEmpty(){
        model.problem = true
    }
    // Hide Empty List
    private fun hideEmpty(){
        model.problem = false
    }

    // Refresh List
    fun refreshList(){
        if (list.size > 0)
            list.clear()
        setupAlarmList()
    }

    fun mutableClick(): MutableLiveData<Any> {
        return liveClick
    }

}