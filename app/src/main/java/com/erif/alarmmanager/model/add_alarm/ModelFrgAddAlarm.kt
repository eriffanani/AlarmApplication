package com.erif.alarmmanager.model.add_alarm

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class ModelFrgAddAlarm: BaseObservable() {

    // ID
    @get: Bindable
    var alarmId: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.alarmId)
        }

    // Alarm Duration
    @get: Bindable
    var alarmDuration: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.alarmDuration)
        }

    // Alarm Title
    @get: Bindable
    var alarmTitle: String? = "Alarm 1"
        set(value) {
            field = value
            notifyPropertyChanged(BR.alarmTitle)
        }

    // Alarm Desc
    @get: Bindable
    var alarmDesc: String? = "Deskripsi Alarm 1"
        set(value) {
            field = value
            notifyPropertyChanged(BR.alarmDesc)
        }

    // Ringtone Title
    @get: Bindable
    var ringtoneTitle: String? = "None"
        set(value) {
            field = value
            notifyPropertyChanged(BR.ringtoneTitle)
        }

    // Ringtone Uri
    @get: Bindable
    var ringtoneUri: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.ringtoneUri)
        }

    // Alarm Desc
    @get: Bindable
    var active: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.active)
        }

    // Vibrate
    @get: Bindable
    var vibrate: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.vibrate)
        }

    // Update
    @get: Bindable
    var update: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.update)
        }

}