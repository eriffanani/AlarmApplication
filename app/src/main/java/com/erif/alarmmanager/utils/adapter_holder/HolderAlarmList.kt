package com.erif.alarmmanager.utils.adapter_holder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.erif.alarmmanager.BR
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.view_model.VMActMain

class HolderAlarmList(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    var binding: ViewDataBinding? = null

    init {
        this.binding = binding
    }

    fun bind(item: ModelItemAlarm, viewModel: VMActMain, position: Int){
        binding?.setVariable(BR.item, item)
        binding?.setVariable(BR.viewModel, viewModel)
        binding?.setVariable(BR.position, position)
        binding?.executePendingBindings()
    }

}