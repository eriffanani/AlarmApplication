package com.erif.alarmmanager.view.add_alarm.form

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.erif.alarmmanager.databinding.ActAddTitleBinding
import com.erif.alarmmanager.model.add_alarm.form.ModelActAddTitle
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.view_model.add_alarm.form.VMActAddTitle

class ActAddTitle : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActAddTitleBinding.inflate(layoutInflater)
        val model = ModelActAddTitle()
        val bundle = intent.extras
        bundle?.let {
            if (it.containsKey("alarmTitle")){
                val alarmTitle = it.getString("alarmTitle")
                alarmTitle?.let { title ->
                    model.title = title
                }
            }
        }
        binding.model = model
        val viewModel = VMActAddTitle()
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(binding.actAddTitleToolbar)

        viewModel.mutableClick().observe(this) {
            if (it == 1) {
                setActivityResult(model)
            }
        }
    }

    private fun setActivityResult(model: ModelActAddTitle) {
        val intent = Intent()
        intent.putExtra("alarmTitle", model.title)
        setResult(Constant.REQUEST_CODE_TITLE, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}