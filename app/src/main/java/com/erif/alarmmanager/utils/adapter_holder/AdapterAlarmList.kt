package com.erif.alarmmanager.utils.adapter_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erif.alarmmanager.databinding.ItemAlarmBinding
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.view_model.VMActMain

class AdapterAlarmList(viewModel: VMActMain) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewModel: VMActMain? = viewModel
    private var list: MutableList<ModelItemAlarm>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderAlarmList(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list?.get(position)
        if (holder is HolderAlarmList){
            if (item != null)
                viewModel?.let { holder.bind(item, it, position) }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setList(list: MutableList<ModelItemAlarm>){
        this.list = list
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        list?.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list?.size!!)
    }

}