package com.erif.alarmmanager.model.add_alarm.form.ringtone

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class ModelActRingtoneChooser: BaseObservable() {

    @get: Bindable
    var selectedTitle: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedTitle)
        }

    @get: Bindable
    var selectedUri: Uri? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedUri)
        }

}