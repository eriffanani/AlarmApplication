package com.erif.alarmmanager.utils.adapter_holder.ringtone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erif.alarmmanager.databinding.ItemRingtoneBinding
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelItemRingtone
import com.erif.alarmmanager.view_model.add_alarm.form.VMActRingtoneChooser

class AdapterRingtoneList constructor(viewModel: VMActRingtoneChooser): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<ModelItemRingtone>? = null
    private var viewModel: VMActRingtoneChooser? = null

    init {
        this.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRingtoneBinding.inflate(inflater, parent, false)
        return HolderRingtoneList(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list?.get(position)
        if (holder is HolderRingtoneList)
            item?.let { viewModel?.let { it1 -> holder.bind(it, it1, position) } }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setList(list: MutableList<ModelItemRingtone>){
        this.list = list
        notifyDataSetChanged()
    }

    fun updateItem(position: Int){
        notifyItemChanged(position)
    }

}