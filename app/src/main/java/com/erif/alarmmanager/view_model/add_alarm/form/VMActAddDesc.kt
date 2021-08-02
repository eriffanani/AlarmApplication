package com.erif.alarmmanager.view_model.add_alarm.form

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.model.add_alarm.form.ModelActAddDesc
import com.erif.alarmmanager.utils.Constant

class VMActAddDesc(context: Context, private val model: ModelActAddDesc): ViewModel() {

    @SuppressLint("StaticFieldLeak")
    private val activity = context as Activity

    fun onClickSave(){
        val intent = Intent()
        intent.putExtra("alarmDesc", model.desc)
        activity.setResult(Constant.REQUEST_CODE_DESC, intent)
        activity.finish()
    }

}