package com.erif.alarmmanager.utils.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.erif.alarmmanager.databinding.ItemRingtoneBinding
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelItemRingtone
import com.erif.alarmmanager.view_model.add_alarm.form.VMActRingtoneChooser

class AdapterRingtoneList constructor(
    private val viewModel: VMActRingtoneChooser
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<ModelItemRingtone> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HolderRingtoneList(
            ItemRingtoneBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is HolderRingtoneList)
            holder.bind(item, viewModel, position)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<ModelItemRingtone>){
        this.list = list
        notifyDataSetChanged()
    }

    fun updateItem(position: Int){
        notifyItemChanged(position)
    }

    private class HolderRingtoneList constructor(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelItemRingtone, viewModel: VMActRingtoneChooser, position: Int){
            binding.setVariable(BR.item, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
        }
    }

}