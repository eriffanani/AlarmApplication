package com.erif.alarmmanager.view_model

import android.app.Dialog
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.utils.callback.CallbackConfirmation

class VMFrgConfirmation(
        private val dialog: Dialog,
        private val callback: CallbackConfirmation) : ViewModel() {

    fun onClickClose(){
        closeDialog()
    }

    fun onClickCancel(){
        closeDialog()
        callback.onConfirmationCancel()
    }

    fun onClickYes(){
        closeDialog()
        callback.onConfirmationYes()
    }

    private fun closeDialog(){
        dialog.dismiss()
    }

}