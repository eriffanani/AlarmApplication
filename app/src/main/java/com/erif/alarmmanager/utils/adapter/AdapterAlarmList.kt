package com.erif.alarmmanager.utils.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.erif.alarmmanager.BR
import com.erif.alarmmanager.databinding.ItemAlarmBinding
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.view_model.VMActMain

class AdapterAlarmList constructor(
    private val viewModel: VMActMain
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<ModelItemAlarm> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HolderAlarmList(
            ItemAlarmBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is HolderAlarmList)
            holder.bind(item, viewModel, position)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<ModelItemAlarm>){
        this.list = list
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    private class HolderAlarmList constructor(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelItemAlarm, viewModel: VMActMain, position: Int){
            binding.setVariable(BR.item, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }

    }

}