package com.erif.alarmmanager.model.add_alarm.form

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class ModelActAddDesc: BaseObservable() {

    @get: Bindable
    var desc: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.desc)
        }

    fun descListener(): TextWatcher{
        return object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                desc = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

}