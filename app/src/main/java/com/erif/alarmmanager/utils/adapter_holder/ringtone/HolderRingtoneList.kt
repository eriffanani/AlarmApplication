package com.erif.alarmmanager.utils.adapter_holder.ringtone

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelItemRingtone
import com.erif.alarmmanager.view_model.add_alarm.form.VMActRingtoneChooser

class HolderRingtoneList(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    private var binding: ViewDataBinding? = null

    init {
        this.binding = binding
    }

    fun bind(item: ModelItemRingtone, viewModel: VMActRingtoneChooser, position: Int){
        binding?.setVariable(BR.item, item)
        binding?.setVariable(BR.viewModel, viewModel)
        binding?.setVariable(BR.position, position)
    }

}