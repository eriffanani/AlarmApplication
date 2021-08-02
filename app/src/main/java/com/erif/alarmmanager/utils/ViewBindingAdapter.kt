package com.erif.alarmmanager.utils

import android.annotation.SuppressLint
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Switch
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("setRecyclerViewAdapter")
fun setRecyclerViewAdapter(recyclerView: RecyclerView, mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>){
    recyclerView.apply {
        layoutManager = LinearLayoutManager(recyclerView.context)
        adapter = mAdapter
    }
}

@BindingAdapter("setNumberPicker")
fun setNumberPicker(picker: NumberPicker, maxValue: Int){
    picker.minValue = 1
    picker.maxValue = maxValue
}

@SuppressLint("UseSwitchCompatOrMaterialCode")
@BindingAdapter("setSwitchListener")
fun setSwitchListener(switch: Switch, listener: CompoundButton.OnCheckedChangeListener){
    switch.setOnCheckedChangeListener(listener)
}

@BindingAdapter("addTextChangedListener")
fun addTextChangedListener(editText: EditText, listener: TextWatcher){
    editText.addTextChangedListener(listener)
}

@BindingAdapter("setNumberPickerListener")
fun setNumberPickerListener(picker: NumberPicker, listener: NumberPicker.OnValueChangeListener){
    picker.setOnValueChangedListener(listener)
}

@BindingAdapter("setNumberPickerValue")
fun setNumberPickerValue(picker: NumberPicker, value: Int){
    picker.value = value
}

@BindingAdapter("setAnimation")
fun setAnimation(view: View, animation: Animation){
    view.startAnimation(animation)
}