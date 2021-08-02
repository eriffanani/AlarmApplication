package com.erif.alarmmanager.view.add_alarm.form.ringtone_chooser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.ActRingtoneChooserBinding
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelActRingtoneChooser
import com.erif.alarmmanager.view_model.add_alarm.form.VMActRingtoneChooser

class ActRingtoneChooser : AppCompatActivity() {

    var viewModel: VMActRingtoneChooser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding(){
        val binding:ActRingtoneChooserBinding = DataBindingUtil.setContentView(this,
                R.layout.act_ringtone_chooser)
        val model = ModelActRingtoneChooser()
        binding.model = model
        viewModel = VMActRingtoneChooser(this, model)
        binding.viewModel = viewModel
        setSupportActionBar(binding.actRingtoneToolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        viewModel?.releasePlayer()
    }

}