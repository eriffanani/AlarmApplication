package com.erif.alarmmanager.view_model

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.widget.CompoundButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.model.ModelActMain
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.adapter_holder.AdapterAlarmList
import com.erif.alarmmanager.utils.callback.CallbackAlarmForm
import com.erif.alarmmanager.utils.callback.CallbackConfirmation
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import com.erif.alarmmanager.view.add_alarm.FrgAddAlarmBottomSheet
import com.erif.alarmmanager.view.add_alarm.form.FrgConfirmationBottomSheet
import org.json.JSONArray
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class VMActMain(
        private val context: Context,
        private val model: ModelActMain,
        private val fragmentManager: FragmentManager
) : ViewModel(), CallbackAlarmForm, CallbackConfirmation {

    private val adapter: AdapterAlarmList = AdapterAlarmList(this)
    private var dialogAdd: FrgAddAlarmBottomSheet? = null
    private val dbAlarm: DatabaseAlarm = DatabaseAlarm(context)
    private var list: MutableList<ModelItemAlarm> = ArrayList()
    private var helper: MyHelper = MyHelper(context)
    private var dialogDelete: FrgConfirmationBottomSheet? = FrgConfirmationBottomSheet(this)
    private var currentID: Int = -1
    private var currentPosition: Int = -1
    private var mLastClickTime: Long = 0

    init {
        requestPermission()
        setupAlarmList()
    }

    // Setup Alarm List
    private fun setupAlarmList(){
        list = dbAlarm.getAlarm()
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
        showFormDialog()
    }

    private fun showForm(){
        dialogAdd?.show(fragmentManager, dialogAdd?.tag)
    }

    private fun showFormDialog(){
        dialogAdd = FrgAddAlarmBottomSheet(this)
        showForm()
    }

    private fun showFormDialog(getAlarmId: Int){
        dialogAdd = FrgAddAlarmBottomSheet(this, true, getAlarmId)
        showForm()
    }

    // Get Adapter
    fun getAdapter(): AdapterAlarmList {
        return adapter
    }

    // Request Permission
    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ANSWER_PHONE_CALLS) !=
                PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                                Manifest.permission.ANSWER_PHONE_CALLS)) {
                    ActivityCompat.requestPermissions(context,
                            arrayOf(Manifest.permission.ANSWER_PHONE_CALLS), 1)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ActivityCompat.requestPermissions(context,
                                arrayOf(Manifest.permission.ANSWER_PHONE_CALLS), 1)
                    }
                }
            }/*else{
                AutoStartHelper.instance.getAutoStartPermission(context)
            }*/
        }
    }

    // On Click Item
    fun onClickItem(item: ModelItemAlarm){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return
        mLastClickTime = SystemClock.elapsedRealtime()
        showFormDialog(item.id)
    }

    // Switch Check Listener
    fun itemCheckListener(item: ModelItemAlarm): CompoundButton.OnCheckedChangeListener{
        return CompoundButton.OnCheckedChangeListener { _, isChecked ->
            item.active = if(isChecked) Constant.STATUS_ACTIVE else Constant.STATUS_NON_ACTIVE
            helper.updateActive(item.id, item.active)
            val millis = helper.getMillis(1, 0)
            if (isChecked) {
                helper.triggerAlarm(item.id, millis, item.duration, true)

                val jsonArray = JSONArray()
                if (item.duration > 1){
                    val lastIndex = item.duration - 1
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
                helper.updateTimeList(item.id, jsonArray.toString())
                helper.updateIndex(item.id, 0)
            }else{
                helper.cancelAlarm(item.id, true)
            }
        }
    }

    // On Click Item Delete
    fun onClickItemDelete(id: Int, position: Int){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return
        mLastClickTime = SystemClock.elapsedRealtime()
        currentID = id
        currentPosition = position
        showDeleteConfirmation()
    }

    // Show Delete Confirmation
    private fun showDeleteConfirmation(){
        dialogDelete?.show(fragmentManager, dialogDelete?.tag)
    }

    // Show Empty List
    private fun showEmpty(){
        model.problem = true
    }
    // Hide Empty List
    private fun hideEmpty(){
        model.problem = false
    }

    // On Click Confirmation Yes
    override fun onConfirmationYes() {
        helper.deleteAlarmFromDatabase(currentID)
        adapter.deleteItem(currentPosition)
        if (list.size < 1)
            showEmpty()
        currentID = -1
        currentPosition = -1
        helper.cancelAlarm(currentID, false)
    }

    // On Click Confirmation Cancel
    override fun onConfirmationCancel() {

    }

    // Update Dialog Ringtone
    fun updateRingtone(ringtoneTitle: String, ringtoneUri: String){
        dialogAdd?.updateRingtone(ringtoneTitle, ringtoneUri)
    }

    // Update Dialog Title
    fun updateTitle(alarmTitle: String){
        dialogAdd?.updateTitle(alarmTitle)
    }

    // Update Dialog Desc
    fun updateDesc(alarmDesc: String){
        dialogAdd?.updateDesc(alarmDesc)
    }

    // Database On Insert New Alarm
    override fun onInsert(id: Int, duration: Int) {
        refreshList()
        val millis = helper.getMillis(1, 0)
        helper.triggerAlarm(id, millis, duration, true)
    }

    // Database On Update Alarm
    override fun onUpdate(id: Int, isUpdateTime: Boolean, duration: Int) {
        refreshList()
        if (isUpdateTime){
            if (helper.isAlarmActive(id)){
                helper.cancelAlarm(id, false)
                val millis = helper.getMillis(1, 0)
                helper.triggerAlarm(id, millis, duration, true)
                helper.updateIndex(id, 0)
            }
        }
    }

    // Refresh List
    private fun refreshList(){
        if (list.size > 0)
            list.clear()
        setupAlarmList()
    }
}