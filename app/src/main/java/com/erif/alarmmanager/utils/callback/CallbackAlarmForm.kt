package com.erif.alarmmanager.utils.callback

interface CallbackAlarmForm {

    fun onInsert(id: Int, duration: Int)
    fun onUpdate(id: Int, isUpdateTime: Boolean, duration: Int)

}