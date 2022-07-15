package com.erif.alarmmanager.view.add_alarm.form

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.erif.alarmmanager.databinding.ActAddDescBinding
import com.erif.alarmmanager.model.add_alarm.form.ModelActAddDesc
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.view_model.add_alarm.form.VMActAddDesc

class ActAddDesc : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActAddDescBinding.inflate(layoutInflater)
        val model = ModelActAddDesc()
        val bundle = intent.extras
        bundle?.let {
            if (it.containsKey("alarmDesc")){
                val alarmDesc = it.getString("alarmDesc")
                alarmDesc?.let { desc ->
                    model.desc = desc
                }
            }
        }
        binding.model = model
        val viewModel = VMActAddDesc()
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(binding.actAddDescToolbar)

        viewModel.mutableClick().observe(this) {
            if (it == 1) {
                setActivityResult(model)
            }
        }
    }

    private fun setActivityResult(model: ModelActAddDesc) {
        val intent = Intent()
        intent.putExtra("alarmDesc", model.desc)
        setResult(Constant.REQUEST_CODE_DESC, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}