package com.erif.alarmmanager.view.receiver

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.ActReceiverBinding
import com.erif.alarmmanager.model.receiver.ModelActReceiver
import com.erif.alarmmanager.utils.services.AlarmRingtoneServices
import com.erif.alarmmanager.view_model.VMActReceiver
import com.liuguangqiang.swipeback.SwipeBackLayout
import com.skyfishjy.library.RippleBackground

class ActReceiver : AppCompatActivity() {

    private lateinit var viewModel: VMActReceiver
    private lateinit var rippleView: RippleBackground

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }
        with(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestDismissKeyguard(this@ActReceiver, null)
            }
        }
        setupBinding()
        val swipeBack = findViewById<SwipeBackLayout>(R.id.swipe_back)
        swipeBack.setDragEdge(SwipeBackLayout.DragEdge.BOTTOM)
        rippleView = findViewById(R.id.rippleBg)
        rippleView.startRippleAnimation()

    }

    private fun setupBinding() {
        val binding:ActReceiverBinding = DataBindingUtil.setContentView(this, R.layout.act_receiver)
        val model = ModelActReceiver()
        binding.model = model
        viewModel = VMActReceiver(this, model)
        binding.viewModel = viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarmMusic()
    }

    private fun stopAlarmMusic(){
        stopService(Intent(this, AlarmRingtoneServices::class.java))
    }

}