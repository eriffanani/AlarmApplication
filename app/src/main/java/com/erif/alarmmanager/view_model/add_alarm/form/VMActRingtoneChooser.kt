package com.erif.alarmmanager.view_model.add_alarm.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelActRingtoneChooser
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelItemRingtone
import com.erif.alarmmanager.utils.adapter.AdapterRingtoneList

class VMActRingtoneChooser constructor(
    private val model: ModelActRingtoneChooser
) : ViewModel() {

    private val adapter = AdapterRingtoneList(this)
    private var list: MutableList<ModelItemRingtone> = ArrayList()
    private var lastPosition: Int = -1

    private val liveClick = MutableLiveData<Any>()

    fun createList(list: MutableList<ModelItemRingtone>) {
        this.list = list
        adapter.setList(list)
    }

    fun onClickItem(item: ModelItemRingtone, position: Int){
        toggleItem(position)
        model.selectedTitle = item.title
        model.selectedUri = item.fileUri
        liveClick.value = item.fileUri
    }

    fun onClickFabDone(){
        liveClick.value = 0
    }

    private fun toggleItem(position: Int){
        if (lastPosition == -1){
            lastPosition = position
            list[position].selected = true
        }else if (lastPosition != position){
            list[lastPosition].selected = false
            list[position].selected = true
            lastPosition = position
        }
    }

    fun getAdapter(): AdapterRingtoneList {
        return adapter
    }

    fun mutableClick(): MutableLiveData<Any> {
        return liveClick
    }

}