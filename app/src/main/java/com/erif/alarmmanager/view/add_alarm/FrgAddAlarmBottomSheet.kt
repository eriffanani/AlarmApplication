package com.erif.alarmmanager.view.add_alarm

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.FrgAddAlarmBottomSheetBinding
import com.erif.alarmmanager.model.add_alarm.ModelFrgAddAlarm
import com.erif.alarmmanager.utils.callback.CallbackAlarmForm
import com.erif.alarmmanager.view_model.add_alarm.VMFrgAddAlarm
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FrgAddAlarmBottomSheet : BottomSheetDialogFragment {

    private var callbackForm: CallbackAlarmForm? = null
    private lateinit var model: ModelFrgAddAlarm
    private var update: Boolean = false
    private var alarmId: Int = 0

    constructor(callbackForm: CallbackAlarmForm){
        this.callbackForm = callbackForm
    }

    constructor(callbackForm: CallbackAlarmForm, update: Boolean, alarmId: Int){
        this.callbackForm = callbackForm
        this.update = update
        this.alarmId = alarmId
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = setupBinding(dialog)
        dialog.setContentView(view)
        return dialog
    }

    private fun setupBinding(dialog: Dialog): View{
        val binding: FrgAddAlarmBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.frg_add_alarm_bottom_sheet, null, false)
        model = ModelFrgAddAlarm()
        model.update = update
        model.alarmId = alarmId
        binding.model = model
        val viewModel = callbackForm?.let { VMFrgAddAlarm(requireContext(), model, dialog, it) }
        binding.viewModel = viewModel
        return binding.root
    }

    fun updateRingtone(ringtoneTitle: String?, ringtoneUri: String?){
        model.ringtoneTitle = ringtoneTitle
        model.ringtoneUri = ringtoneUri
    }

    fun updateTitle(alarmTitle: String?){
        model.alarmTitle = alarmTitle
    }

    fun updateDesc(alarmDesc: String?){
        model.alarmDesc = alarmDesc
    }

}