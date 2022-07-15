package com.erif.alarmmanager.view_model.add_alarm.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VMActAddDesc: ViewModel() {

    private val liveClick = MutableLiveData<Int>()

    fun onClickSave(){
        liveClick.value = 1
    }

    fun mutableClick(): MutableLiveData<Int> {
        return liveClick
    }

}