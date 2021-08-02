package com.erif.alarmmanager.model.add_alarm.form.ringtone

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class ModelItemRingtone: BaseObservable() {

    @get: Bindable
    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get: Bindable
    var fileUri: Uri? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.fileUri)
        }

    @get: Bindable
    var selected: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

}