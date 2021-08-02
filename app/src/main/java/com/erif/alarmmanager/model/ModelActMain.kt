package com.erif.alarmmanager.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.erif.alarmmanager.BR

class ModelActMain: BaseObservable() {

    @get: Bindable
    var problem: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.problem)
        }

}