package com.erif.alarmmanager.view.add_alarm

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.FrgAddAlarmBottomSheetBinding
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.model.add_alarm.ModelFrgAddAlarm
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.callback.CallbackAlarmForm
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import com.erif.alarmmanager.view.add_alarm.form.ActAddDesc
import com.erif.alarmmanager.view.add_alarm.form.ActAddTitle
import com.erif.alarmmanager.view.add_alarm.form.ringtone_chooser.ActRingtoneChooser
import com.erif.alarmmanager.view_model.add_alarm.VMFrgAddAlarm
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FrgAddAlarmBottomSheet : BottomSheetDialogFragment {

    private var callbackForm: CallbackAlarmForm? = null
    private lateinit var model: ModelFrgAddAlarm
    private var update: Boolean = false
    private var alarmId: Int = 0

    private var dbAlarm: DatabaseAlarm? = null
    private var helper: MyHelper? = null

    private var launcher: ActivityResultLauncher<Intent>? = null

    constructor()

    constructor(callbackForm: CallbackAlarmForm?, launcher: ActivityResultLauncher<Intent>?): this() {
        this.callbackForm = callbackForm
        this.launcher = launcher
    }

    constructor(callbackForm: CallbackAlarmForm?, update: Boolean, alarmId: Int, launcher: ActivityResultLauncher<Intent>?): this() {
        this.callbackForm = callbackForm
        this.update = update
        this.alarmId = alarmId
        this.launcher = launcher
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = setupBinding()
        dialog.setContentView(view)
        return dialog
    }

    private fun setupBinding(): View{
        val binding = FrgAddAlarmBottomSheetBinding.inflate(
            layoutInflater, null, false
        )
        dbAlarm = DatabaseAlarm(requireContext())
        helper = MyHelper(requireContext())

        model = ModelFrgAddAlarm()
        model.ringtoneTitle = getString(R.string.none)
        model.alarmTitle = getString(R.string.alarm_title)
        model.alarmDesc = getString(R.string.alarm_desc)
        model.update = update
        model.alarmId = alarmId
        binding.model = model
        val viewModel = VMFrgAddAlarm(model, dbAlarm, helper)
        binding.viewModel = viewModel

        viewModel.mutableClick().observe(this) {
            if (it is Int) {
                when (it) {
                    0 -> {
                        this.dismiss()
                    }
                    1 -> {
                        launcher?.launch(
                            Intent(requireContext(), ActRingtoneChooser::class.java)
                        )
                    }
                    2 -> {
                        val intent = Intent(requireContext(), ActAddTitle::class.java)
                        intent.putExtra("alarmTitle", model.alarmTitle)
                        launcher?.launch(intent)
                    }
                    3 -> {
                        val intent = Intent(requireContext(), ActAddDesc::class.java)
                        intent.putExtra("alarmDesc", model.alarmDesc)
                        launcher?.launch(intent)
                    }
                }
            } else if (it is ModelItemAlarm) {
                if (model.update) {
                    model.alarmId.let { id ->
                        val getIsDurationUpdated = helper?.isDurationUpdated(id, model.alarmDuration) ?: false
                        dbAlarm?.update(id, it)
                        callbackForm?.onUpdate(id, getIsDurationUpdated, model.alarmDuration)
                        dismiss()
                    }
                }else{
                    dbAlarm?.insert(it)
                    val id: Int = dbAlarm?.getLastID() ?: 0
                    callbackForm?.onInsert(id, model.alarmDuration)
                    dismiss()
                }
            }
        }
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