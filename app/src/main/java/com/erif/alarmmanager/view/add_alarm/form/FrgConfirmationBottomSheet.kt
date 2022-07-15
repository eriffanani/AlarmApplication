package com.erif.alarmmanager.view.add_alarm.form

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.erif.alarmmanager.databinding.FrgConfirmationBottomSheetBinding
import com.erif.alarmmanager.utils.callback.CallbackConfirmation
import com.erif.alarmmanager.view_model.VMFrgConfirmation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FrgConfirmationBottomSheet: BottomSheetDialogFragment {

    private var callback: CallbackConfirmation? = null

    constructor()

    constructor(callback: CallbackConfirmation?) {
        this.callback = callback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = setupBinding()
        dialog.setContentView(view)
        return dialog
    }

    private fun setupBinding(): View{
        val binding = FrgConfirmationBottomSheetBinding.inflate(
            layoutInflater, null, false
        )
        val viewModel = VMFrgConfirmation()
        binding.viewModel = viewModel

        viewModel.mutableClick().observe(this) {
            when (it) {
                0 -> {
                    this.dismiss()
                }
                1 -> {
                    callback?.onConfirmationCancel()
                    this.dismiss()
                }
                2 -> {
                    callback?.onConfirmationYes()
                    this.dismiss()
                }
            }
        }

        return binding.root
    }

}