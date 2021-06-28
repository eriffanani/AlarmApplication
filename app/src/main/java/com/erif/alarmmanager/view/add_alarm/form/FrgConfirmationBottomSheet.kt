package com.erif.alarmmanager.view.add_alarm.form

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.FrgConfirmationBottomSheetBinding
import com.erif.alarmmanager.utils.callback.CallbackConfirmation
import com.erif.alarmmanager.view_model.VMFrgConfirmation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FrgConfirmationBottomSheet(callback: CallbackConfirmation) : BottomSheetDialogFragment() {

    private var callback: CallbackConfirmation? = null

    init {
        this.callback = callback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = setupBinding(dialog)
        dialog.setContentView(view)
        return dialog
    }

    private fun setupBinding(dialog: Dialog): View{
        val binding: FrgConfirmationBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()), R.layout.frg_confirmation_bottom_sheet, null, false)
        val viewModel = VMFrgConfirmation(dialog, callback!!)
        binding.viewModel = viewModel
        return binding.root
    }

}