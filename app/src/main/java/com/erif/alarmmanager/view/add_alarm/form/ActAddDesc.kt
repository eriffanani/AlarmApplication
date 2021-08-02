package com.erif.alarmmanager.view.add_alarm.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.ActAddDescBinding
import com.erif.alarmmanager.model.add_alarm.form.ModelActAddDesc
import com.erif.alarmmanager.view_model.add_alarm.form.VMActAddDesc

class ActAddDesc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        val binding: ActAddDescBinding = DataBindingUtil.setContentView(this, R.layout.act_add_desc)
        val model = ModelActAddDesc()
        val bundle = intent.extras
        bundle?.let {
            if (it.containsKey("alarmDesc")){
                val alarmDesc = it.getString("alarmDesc")
                alarmDesc?.let {
                    it1->
                    model.desc = it1
                }
            }
        }
        binding.model = model
        val viewModel = VMActAddDesc(this, model)
        binding.viewModel = viewModel
        setSupportActionBar(binding.actAddDescToolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}