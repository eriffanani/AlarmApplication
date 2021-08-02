package com.erif.alarmmanager.view.add_alarm.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.ActAddTitleBinding
import com.erif.alarmmanager.model.add_alarm.form.ModelActAddTitle
import com.erif.alarmmanager.view_model.add_alarm.form.VMActAddTitle

class ActAddTitle : AppCompatActivity() {

    private lateinit var model: ModelActAddTitle
    private lateinit var viewModel: VMActAddTitle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        val binding: ActAddTitleBinding = DataBindingUtil.setContentView(this, R.layout.act_add_title)
        model = ModelActAddTitle()
        val bundle = intent.extras
        bundle?.let {
            if (bundle.containsKey("alarmTitle")){
                val alarmTitle = bundle.getString("alarmTitle")
                alarmTitle?.let {
                    model.title = it
                }
            }
        }
        binding.model = model
        viewModel = VMActAddTitle(this, model)
        binding.viewModel = viewModel
        setSupportActionBar(binding.actAddTitleToolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}