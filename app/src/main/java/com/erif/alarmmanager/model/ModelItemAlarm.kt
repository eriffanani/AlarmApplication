package com.erif.alarmmanager.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.erif.alarmmanager.BR

class ModelItemAlarm: BaseObservable() {

    // ID
    var id: Int = 0

    // Title
    @get: Bindable
    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    // Desc
    @get: Bindable
    var desc: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.desc)
        }

    // Duration
    @get: Bindable
    var duration: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.duration)
        }

    // Ringtone Title
    @get: Bindable
    var ringtoneTitle: String? = null
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

    // Time List
    @get: Bindable
    var timeList: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.timeList)
        }

    // Index
    @get: Bindable
    var index: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.index)
        }

    // Vibrate
    @get: Bindable
    var vibrate: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.vibrate)
        }

    // Active
    @get: Bindable
    var active: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.active)
        }

}