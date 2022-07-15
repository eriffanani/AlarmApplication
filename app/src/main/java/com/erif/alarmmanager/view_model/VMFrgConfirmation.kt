package com.erif.alarmmanager.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VMFrgConfirmation: ViewModel() {

    private val liveClick = MutableLiveData<Int>()

    fun onClickClose() {
        liveClick.value = 0
    }

    fun onClickCancel() {
        liveClick.value = 1
    }

    fun onClickYes() {
        liveClick.value = 2
    }

    fun mutableClick(): MutableLiveData<Int> {
        return liveClick
    }

}