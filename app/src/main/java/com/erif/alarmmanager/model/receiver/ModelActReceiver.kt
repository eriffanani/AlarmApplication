package com.erif.alarmmanager.model.receiver

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class ModelActReceiver: BaseObservable() {

    // Title
    @get: Bindable
    var alarmTitle: String? = null
        set(value) {
            field  = value
            notifyPropertyChanged(BR.alarmTitle)
        }

    // Desc
    @get: Bindable
    var alarmDesc: String? = null
        set(value) {
            field  = value
            notifyPropertyChanged(BR.alarmDesc)
        }

    @get: Bindable
    var currentTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentTime)
        }

}